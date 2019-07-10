package org.smartregister.chw.anc.intent;

import android.app.IntentService;
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
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.Util;
import org.smartregister.chw.anc.util.Utils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.repository.AllSharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class HomeVisitIntent extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private VisitRepository visitRepository;
    private VisitDetailsRepository visitDetailsRepository;

    public HomeVisitIntent() {
        super("HomeVisitService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            processVisits();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        visitRepository = AncLibrary.getInstance().visitRepository();
        visitDetailsRepository = AncLibrary.getInstance().visitDetailsRepository();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Process all the visit older than 24 hours
     *
     * @throws Exception
     */
    protected void processVisits() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -24);

        List<Visit> visits = visitRepository.getAllUnSynced(calendar.getTime().getTime());
        for (Visit v : visits) {
            if (!v.getProcessed()) {

                // process details
                processVisitDetails(v.getVisitId(), v.getBaseEntityId());

                // persist to db
                Event baseEvent = new Gson().fromJson(v.getPreProcessedJson(), Event.class);
                AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().context().allSharedPreferences();
                Util.processEvent(allSharedPreferences, baseEvent);

                visitRepository.completeProcessing(v.getVisitId());
            }
        }
    }

    private void processVisitDetails(String visitID, String baseEntityID) {
        List<VisitDetail> visitDetailList = visitDetailsRepository.getVisits(visitID);
        List<VaccineWrapper> vaccineWrappers = new ArrayList<>();
        List<ServiceWrapper> serviceWrappers = new ArrayList<>();

        for (VisitDetail visitDetail : visitDetailList) {
            if (!visitDetail.getProcessed()) {

                if (StringUtils.isNotBlank(visitDetail.getPreProcessedType()) && StringUtils.isNotBlank(visitDetail.getPreProcessedJson())) {
                    switch (visitDetail.getPreProcessedType()) {
                        case "vaccine":
                            vaccineWrappers.add(new Gson().fromJson(visitDetail.getPreProcessedJson(), VaccineWrapper.class));
                            break;
                        case "service":
                            Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
                            serviceWrappers.add(gson.fromJson(visitDetail.getPreProcessedJson(), ServiceWrapper.class));
                            break;
                        default:
                            break;
                    }
                }
            }
            visitDetailsRepository.completeProcessing(visitDetail.getVisitId());
        }

        if (vaccineWrappers.size() > 0)
            saveVaccines(vaccineWrappers, baseEntityID);

        if (serviceWrappers.size() > 0)
            saveServices(serviceWrappers, baseEntityID);

    }

    protected void saveVaccines(List<VaccineWrapper> tags, String baseEntityID) {
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

    protected VaccineRepository getVaccineRepository() {
        return ImmunizationLibrary.getInstance().vaccineRepository();
    }

    protected void saveServices(List<ServiceWrapper> tags, String baseEntityId) {
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
