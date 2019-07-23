package org.smartregister.chw.anc.util;

import android.content.Context;
import android.content.Intent;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.service.intent.RecurringIntentService;
import org.smartregister.immunization.service.intent.VaccineIntentService;
import org.smartregister.repository.AllSharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitUtils {

    public static List<Visit> getVisits(String memberID) {

        List<Visit> visits = getVisitsOnly(memberID);

        int x = 0;
        while (visits.size() > x) {
            Visit visit = visits.get(x);
            List<VisitDetail> detailList = getVisitDetailsOnly(visit.getVisitId());
            visits.get(x).setVisitDetails(getVisitGroups(detailList));
            x++;
        }

        return visits;
    }

    public static List<Visit> getVisitsOnly(String memberID) {
        return new ArrayList<>(AncLibrary.getInstance().visitRepository().getVisits(memberID, Constants.EVENT_TYPE.ANC_HOME_VISIT));
    }

    public static List<VisitDetail> getVisitDetailsOnly(String visitID) {
        return AncLibrary.getInstance().visitDetailsRepository().getVisits(visitID);
    }

    public static Map<String, List<VisitDetail>> getVisitGroups(List<VisitDetail> detailList) {
        Map<String, List<VisitDetail>> visitMap = new HashMap<>();

        for (VisitDetail visitDetail : detailList) {

            List<VisitDetail> visitDetailList = visitMap.get(visitDetail.getVisitKey());
            if (visitDetailList == null)
                visitDetailList = new ArrayList<>();

            visitDetailList.add(visitDetail);

            visitMap.put(visitDetail.getVisitKey(), visitDetailList);
        }
        return visitMap;
    }

    /**
     * To be invoked for manual processing
     *
     * @param baseEntityID
     * @throws Exception
     */
    public static void processVisits(String baseEntityID) throws Exception {
        processVisits(AncLibrary.getInstance().visitRepository(), AncLibrary.getInstance().visitDetailsRepository(), baseEntityID);
    }

    public static void processVisits(VisitRepository visitRepository, VisitDetailsRepository visitDetailsRepository, String baseEntityID) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -24);

        List<Visit> visits = StringUtils.isNotBlank(baseEntityID) ?
                visitRepository.getAllUnSynced(calendar.getTime().getTime(), baseEntityID) :
                visitRepository.getAllUnSynced(calendar.getTime().getTime());
        for (Visit v : visits) {
            if (!v.getProcessed()) {

                // process details
                processVisitDetails(visitDetailsRepository, v.getVisitId(), v.getBaseEntityId());

                // persist to db
                Event baseEvent = new Gson().fromJson(v.getPreProcessedJson(), Event.class);
                AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().context().allSharedPreferences();
                Util.addEvent(allSharedPreferences, baseEvent);

                visitRepository.completeProcessing(v.getVisitId());
            }
        }

        // process after all events are saved
        Util.startClientProcessing();

        // process vaccines and services
        Context context = AncLibrary.getInstance().context().applicationContext();
        context.startService(new Intent(context, VaccineIntentService.class));
        context.startService(new Intent(context, RecurringIntentService.class));
    }

    private static void processVisitDetails(VisitDetailsRepository visitDetailsRepository, String visitID, String baseEntityID) {
        List<VisitDetail> visitDetailList = visitDetailsRepository.getVisits(visitID);
        List<VaccineWrapper> vaccineWrappers = new ArrayList<>();
        List<ServiceWrapper> serviceWrappers = new ArrayList<>();

        Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
        for (VisitDetail visitDetail : visitDetailList) {
            if (!visitDetail.getProcessed()) {

                if (StringUtils.isNotBlank(visitDetail.getPreProcessedType()) && StringUtils.isNotBlank(visitDetail.getPreProcessedJson())) {
                    switch (visitDetail.getPreProcessedType()) {
                        case "vaccine":
                            vaccineWrappers.add(gson.fromJson(visitDetail.getPreProcessedJson(), VaccineWrapper.class));
                            break;
                        case "service":
                            serviceWrappers.add(gson.fromJson(visitDetail.getPreProcessedJson(), ServiceWrapper.class));
                            break;
                        default:
                            break;
                    }
                }
                visitDetailsRepository.completeProcessing(visitDetail.getVisitDetailsId());
            }
        }

        if (vaccineWrappers.size() > 0)
            saveVaccines(vaccineWrappers, baseEntityID);

        if (serviceWrappers.size() > 0)
            saveServices(serviceWrappers, baseEntityID);

    }

    protected static void saveVaccines(List<VaccineWrapper> tags, String baseEntityID) {
        for (VaccineWrapper tag : tags) {
            if (tag.getUpdatedVaccineDate() == null) {
                return;
            }
            Vaccine vaccine = new Vaccine();
            if (tag.getDbKey() != null) {
                vaccine = getVaccineRepository().find(tag.getDbKey());
            }
            vaccine.setBaseEntityId(baseEntityID);
            vaccine.setName(tag.getName());
            vaccine.setDate(tag.getUpdatedVaccineDate().toDate());
            vaccine.setCreatedAt(tag.getUpdatedVaccineDate().toDate());

            String lastChar = vaccine.getName().substring(vaccine.getName().length() - 1);
            if (StringUtils.isNumeric(lastChar)) {
                vaccine.setCalculation(Integer.valueOf(lastChar));
            } else {
                vaccine.setCalculation(-1);
            }

            JsonFormUtils.tagSyncMetadata(Utils.context().allSharedPreferences(), vaccine);
            getVaccineRepository().add(vaccine); // persist to local db
            tag.setDbKey(vaccine.getId());
        }
    }

    protected static VaccineRepository getVaccineRepository() {
        return ImmunizationLibrary.getInstance().vaccineRepository();
    }

    protected static void saveServices(List<ServiceWrapper> tags, String baseEntityId) {
        for (ServiceWrapper tag : tags) {
            if (tag.getUpdatedVaccineDate() == null) {
                return;
            }

            RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();

            ServiceRecord serviceRecord = new ServiceRecord();
            if (tag.getDbKey() != null) {
                serviceRecord = recurringServiceRecordRepository.find(tag.getDbKey());
                if (serviceRecord == null) {
                    serviceRecord = new ServiceRecord();
                    serviceRecord.setDate(tag.getUpdatedVaccineDate().toDate());

                    serviceRecord.setBaseEntityId(baseEntityId);
                    serviceRecord.setRecurringServiceId(tag.getTypeId());
                    serviceRecord.setDate(tag.getUpdatedVaccineDate().toDate());
                    serviceRecord.setValue(tag.getValue());
                    serviceRecord.setCreatedAt(tag.getUpdatedVaccineDate().toDate());

                    JsonFormUtils.tagSyncMetadata(Utils.context().allSharedPreferences(), serviceRecord);
                } else {
                    serviceRecord.setDate(tag.getUpdatedVaccineDate().toDate());
                    serviceRecord.setValue(tag.getValue());
                }

            } else {
                serviceRecord.setDate(tag.getUpdatedVaccineDate().toDate());

                serviceRecord.setBaseEntityId(baseEntityId);
                serviceRecord.setRecurringServiceId(tag.getTypeId());
                serviceRecord.setDate(tag.getUpdatedVaccineDate().toDate());
                serviceRecord.setValue(tag.getValue());

                JsonFormUtils.tagSyncMetadata(Utils.context().allSharedPreferences(), serviceRecord);
            }

            recurringServiceRecordRepository.add(serviceRecord);
            tag.setDbKey(serviceRecord.getId());
        }
    }
}
