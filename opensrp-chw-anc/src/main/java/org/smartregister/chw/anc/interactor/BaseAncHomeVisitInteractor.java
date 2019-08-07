package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.actionhelper.DangerSignsHelper;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.Util;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;
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
                    BaseAncHomeVisitAction ba =
                            new BaseAncHomeVisitAction.Builder(view.getContext(), "Sample Action")
                                    .withSubtitle("")
                                    .withOptional(false)
                                    .withFormName("anc")
                                    .withHelper(new DangerSignsHelper())
                                    .build();
                    actionList.put("Sample Action", ba);

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
    public void submitVisit(final boolean editMode, final String memberID, final Map<String, BaseAncHomeVisitAction> map, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                boolean result = true;
                try {

                    Map<String, String> jsons = new HashMap<>();
                    Map<String, VaccineWrapper> vaccineWrapperMap = new HashMap<>();
                    Map<String, ServiceWrapper> serviceWrapperMap = new HashMap<>();

                    // aggregate forms to be processed
                    for (Map.Entry<String, BaseAncHomeVisitAction> entry : map.entrySet()) {
                        String json = entry.getValue().getJsonPayload();
                        if (StringUtils.isNotBlank(json)) {
                            jsons.put(entry.getKey(), json);
                            JSONObject jsonObject = new JSONObject(json);
                            if (entry.getValue().getVaccineWrapper() != null) {
                                int position = 0;
                                for (VaccineWrapper v : entry.getValue().getVaccineWrapper()) {
                                    vaccineWrapperMap.put(JsonFormUtils.getObjectKey(jsonObject, position), v);
                                    position++;
                                }
                            }
                            if (entry.getValue().getServiceWrapper() != null) {
                                int position = 0;
                                for (ServiceWrapper sw : entry.getValue().getServiceWrapper()) {
                                    serviceWrapperMap.put(JsonFormUtils.getObjectKey(jsonObject, position), sw);
                                    position++;
                                }
                            }
                        }
                    }

                    saveVisit(editMode, memberID, getEncounterType(), jsons, vaccineWrapperMap, serviceWrapperMap);

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

    private void saveVisit(boolean editMode, String memberID, String encounterType,
                           final Map<String, String> jsonString,
                           Map<String, VaccineWrapper> vaccineWrapperMap,
                           Map<String, ServiceWrapper> serviceWrapperMap
    ) throws Exception {

        AllSharedPreferences allSharedPreferences = AncLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = JsonFormUtils.processAncJsonForm(allSharedPreferences, memberID, encounterType, jsonString);
        prepareEvent(baseEvent);
        if (baseEvent != null) {
            baseEvent.setFormSubmissionId(JsonFormUtils.generateRandomUUIDString());
            JsonFormUtils.tagEvent(allSharedPreferences, baseEvent);

            String visitID = (editMode) ?
                    AncLibrary.getInstance().visitRepository().getLatestVisit(memberID, getEncounterType()).getVisitId() :
                    JsonFormUtils.generateRandomUUIDString();


            // reset database
            if (editMode) {
                AncLibrary.getInstance().visitRepository().deleteVisit(visitID);
                AncLibrary.getInstance().visitDetailsRepository().deleteVisitDetails(visitID);
            }

            Visit visit = Util.eventToVisit(baseEvent, visitID);
            visit.setPreProcessedJson(new Gson().toJson(baseEvent));
            AncLibrary.getInstance().visitRepository().addVisit(visit);

            if (visit.getVisitDetails() != null) {
                Gson gson = Converters.registerDateTime(new GsonBuilder()).create();

                for (Map.Entry<String, List<VisitDetail>> entry : visit.getVisitDetails().entrySet()) {
                    if (entry.getValue() != null) {
                        for (VisitDetail d : entry.getValue()) {

                            VaccineWrapper vaccineWrapper = vaccineWrapperMap.get(d.getVisitKey());
                            if (vaccineWrapper != null) {
                                String json = gson.toJson(vaccineWrapper);
                                d.setPreProcessedJson(json);
                                d.setPreProcessedType("vaccine");
                            }

                            ServiceWrapper serviceWrapper = serviceWrapperMap.get(d.getVisitKey());
                            if (serviceWrapper != null) {
                                String json = gson.toJson(serviceWrapper);
                                d.setPreProcessedJson(json);
                                d.setPreProcessedType("service");
                            }

                            AncLibrary.getInstance().visitDetailsRepository().addVisitDetails(d);
                        }
                    }
                }
            }
        }
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

    protected String getEncounterType() {
        return Constants.EVENT_TYPE.ANC_HOME_VISIT;
    }

}
