package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.Util;
import org.smartregister.chw.anc.util.Utils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.repository.AllSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class BaseAncHomeVisitInteractor implements BaseAncHomeVisitContract.Interactor {

    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseAncHomeVisitInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAncHomeVisitInteractor() {
        this(new AppExecutors());
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

    @Override
    public void saveRegistration(String jsonString, boolean isEditMode, BaseAncHomeVisitContract.InteractorCallBack callBack) {
        Timber.v("saveRegistration");
    }

    @Override
    public void calculateActions(final BaseAncHomeVisitContract.View view, MemberObject memberObject, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final LinkedHashMap<String, BaseAncHomeVisitAction> actionList = new LinkedHashMap<>();

                try {

                    actionList.put("Sample Action", new BaseAncHomeVisitAction("Sample Action", "Override class org.smartregister.chw.anc.interactor.BaseAncHomeVisitInteractor", false,
                            null, "anc"));

                } catch (BaseAncHomeVisitAction.ValidationException e) {
                    Timber.e(e);
                }

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.preloadActions(actionList);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void submitVisit(final String memberID, final Map<String, BaseAncHomeVisitAction> map, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                boolean result = true;
                try {


                    Map<String, String> jsons = new HashMap<>();
                    List<VaccineWrapper> vaccineWrappers = new ArrayList<>();
                    List<ServiceWrapper> serviceWrappers = new ArrayList<>();

                    // aggregate forms to be processed
                    for (Map.Entry<String, BaseAncHomeVisitAction> entry : map.entrySet()) {
                        String json = entry.getValue().getJsonPayload();
                        if (StringUtils.isNotBlank(json)) {
                            jsons.put(entry.getKey(), json);
                        }
                        if (entry.getValue().getVaccineWrapper() != null) {
                            vaccineWrappers.add(entry.getValue().getVaccineWrapper());
                        }
                        if (entry.getValue().getServiceWrapper() != null) {
                            serviceWrappers.add(entry.getValue().getServiceWrapper());
                        }
                    }

                    saveVisit(memberID, getEncounterType(), jsons);
                    saveVaccines(vaccineWrappers, memberID);
                    saveServices(serviceWrappers, memberID);

                } catch (Exception e) {
                    Timber.e(e);
                    result = false;
                }

                final boolean finalResult = result;
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSubmitted(finalResult);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    private void saveVisit(String memberID, String encounterType, final Map<String, String> jsonString) throws Exception {

        AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = JsonFormUtils.processAncJsonForm(allSharedPreferences, memberID, encounterType, jsonString);
        prepareEvent(baseEvent);
        Util.processEvent(allSharedPreferences, baseEvent);
    }

    /**
     * Injects implementation specific changes to the event
     *
     * @param baseEvent
     */
    protected void prepareEvent(Event baseEvent) {
        if (baseEvent != null) {
            // add anc date obs and last
            List<Object> list = new ArrayList<>();
            list.add(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
            baseEvent.addObs(new Obs("concept", "text", "anc_visit_date", "",
                    list, new ArrayList<>(), null, "anc_visit_date"));
        }
    }

    public String getTableName() {
        return Constants.TABLES.FAMILY_MEMBER;
    }

    public CommonRepository getCommonRepository(String tableName) {
        return AncLibrary.getInstance().context().commonrepository(tableName);
    }

    protected String getEncounterType() {
        return Constants.EVENT_TYPE.ANC_HOME_VISIT;
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
}
