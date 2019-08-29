package org.smartregister.chw.anc.util;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.service.intent.RecurringIntentService;
import org.smartregister.immunization.service.intent.VaccineIntentService;
import org.smartregister.repository.AllSharedPreferences;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitUtils {

    public static List<Visit> getVisits(String memberID, String... eventTypes) {

        List<Visit> visits = (eventTypes != null && eventTypes.length > 0) ? getVisitsOnly(memberID, eventTypes[0]) : getVisitsOnly(memberID, Constants.EVENT_TYPE.ANC_HOME_VISIT);

        int x = 0;
        while (visits.size() > x) {
            Visit visit = visits.get(x);
            List<VisitDetail> detailList = getVisitDetailsOnly(visit.getVisitId());
            visits.get(x).setVisitDetails(getVisitGroups(detailList));
            x++;
        }

        return visits;
    }

    public static List<Visit> getVisitsOnly(String memberID, String visitName) {
        return new ArrayList<>(AncLibrary.getInstance().visitRepository().getVisits(memberID, visitName));
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
                processVisitDetails(v, visitDetailsRepository, v.getVisitId(), v.getBaseEntityId());

                // persist to db
                Event baseEvent = new Gson().fromJson(v.getPreProcessedJson(), Event.class);
                AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().context().allSharedPreferences();
                NCUtils.addEvent(allSharedPreferences, baseEvent);

                visitRepository.completeProcessing(v.getVisitId());
            }
        }

        // process after all events are saved
        NCUtils.startClientProcessing();

        // process vaccines and services
        Context context = AncLibrary.getInstance().context().applicationContext();
        context.startService(new Intent(context, VaccineIntentService.class));
        context.startService(new Intent(context, RecurringIntentService.class));
    }

    private static void processVisitDetails(Visit visit, VisitDetailsRepository visitDetailsRepository, String visitID, String baseEntityID) throws Exception {
        List<VisitDetail> visitDetailList = visitDetailsRepository.getVisits(visitID);
        for (VisitDetail visitDetail : visitDetailList) {
            if (!visitDetail.getProcessed()) {
                if (Constants.HOME_VISIT_TASK.SERVICE.equalsIgnoreCase(visitDetail.getPreProcessedType())) {
                    saveVisitDetailsAsServiceRecord(visitDetail, baseEntityID, visit.getDate());
                    visitDetailsRepository.completeProcessing(visitDetail.getVisitDetailsId());
                    continue;
                }


                if (Constants.HOME_VISIT_TASK.VACCINE.equalsIgnoreCase(visitDetail.getParentCode())) {
                    saveVisitDetailsAsVaccine(visitDetail, baseEntityID, visit.getDate());
                    visitDetailsRepository.completeProcessing(visitDetail.getVisitDetailsId());
                    continue;
                }

                visitDetailsRepository.completeProcessing(visitDetail.getVisitDetailsId());
            }
        }
    }

    public static Vaccine saveVisitDetailsAsVaccine(VisitDetail detail, String baseEntityID, Date eventDate) {
        if (!"vaccine".equalsIgnoreCase(detail.getParentCode()))
            return null;

        if (Constants.HOME_VISIT.VACCINE_NOT_GIVEN.equalsIgnoreCase(detail.getParentCode()))
            return null;

        Date vacDate = getDateFromString(detail.getDetails());
        if (vacDate == null) return null;

        Vaccine vaccine = new Vaccine();
        vaccine.setBaseEntityId(baseEntityID);
        vaccine.setName(detail.getVisitKey());
        vaccine.setDate(vacDate);
        vaccine.setCreatedAt(eventDate);

        String lastChar = vaccine.getName().substring(vaccine.getName().length() - 1);
        if (StringUtils.isNumeric(lastChar)) {
            vaccine.setCalculation(Integer.valueOf(lastChar));
        } else {
            vaccine.setCalculation(0);
        }

        JsonFormUtils.tagSyncMetadata(NCUtils.context().allSharedPreferences(), vaccine);
        getVaccineRepository().add(vaccine); // persist to local db

        return vaccine;
    }

    public static ServiceRecord saveVisitDetailsAsServiceRecord(VisitDetail detail, String baseEntityID, Date eventDate) {
        String val = NCUtils.getText(detail).trim();
        if (Constants.HOME_VISIT.DOSE_NOT_GIVEN.equalsIgnoreCase(val)) return null;

        RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
        List<ServiceType> serviceTypes = recurringServiceTypeRepository.searchByName(detail.getPreProcessedJson());
        if (serviceTypes.size() != 1) return null;

        ServiceType serviceType = serviceTypes.get(0);

        RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setDate(eventDate);

        serviceRecord.setBaseEntityId(baseEntityID);
        serviceRecord.setRecurringServiceId(serviceType.getId());
        serviceRecord.setValue("yes");

        JsonFormUtils.tagSyncMetadata(NCUtils.context().allSharedPreferences(), serviceRecord);
        recurringServiceRecordRepository.add(serviceRecord);

        return serviceRecord;
    }

    public static Date getDateFromString(String dateStr) {
        try {
            return NCUtils.getSaveDateFormat().parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void saveVaccines(List<VaccineWrapper> tags, String baseEntityID) {
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
                vaccine.setCalculation(0);
            }

            JsonFormUtils.tagSyncMetadata(NCUtils.context().allSharedPreferences(), vaccine);
            getVaccineRepository().add(vaccine); // persist to local db
            tag.setDbKey(vaccine.getId());
        }
    }

    public static VaccineRepository getVaccineRepository() {
        return ImmunizationLibrary.getInstance().vaccineRepository();
    }

    /**
     * Check whether a visit occurred in the last 24 hours
     *
     * @param lastVisit The Visit instance for which you wish to check
     * @return true or false based on whether the visit was between 24 hours
     */
    public static boolean isVisitWithin24Hours(Visit lastVisit) {
        if (lastVisit != null) {
            return (Days.daysBetween(new DateTime(lastVisit.getCreatedAt()), new DateTime()).getDays() < 1) &&
                    (Days.daysBetween(new DateTime(lastVisit.getDate()), new DateTime()).getDays() <= 1);
        }
        return false;
    }
}
