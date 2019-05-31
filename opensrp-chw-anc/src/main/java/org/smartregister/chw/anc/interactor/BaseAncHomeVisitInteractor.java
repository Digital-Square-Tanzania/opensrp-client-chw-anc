package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONObject;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.util.Utils.getAllSharedPreferences;
import static org.smartregister.util.Utils.getName;

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
    public void getUserInformation(final String memberID, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String patientName = "";
                String age = "";
                try {
                    CommonPersonObject personObject = getCommonRepository(getTableName()).findByBaseEntityId(memberID);
                    CommonPersonObjectClient pc = new CommonPersonObjectClient(personObject.getCaseId(),
                            personObject.getDetails(), "");
                    pc.setColumnmaps(personObject.getColumnmaps());

                    String fname = getName(
                            Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true),
                            Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.MIDDLE_NAME, true)
                    );

                    patientName = getName(fname, Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_NAME, true));
                    String dobString = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DOB, false);

                    age = String.valueOf(new Period(new DateTime(dobString), new DateTime()).getYears());

                } catch (Exception e) {
                    Timber.e(e);
                }

                final String finalPatientName = patientName;
                final String finalAge = age;
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onMemberDetailsLoaded(finalPatientName, finalAge);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void calculateActions(final BaseAncHomeVisitContract.View view, String memberID, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
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

                    // aggregate forms to be processed
                    for (Map.Entry<String, BaseAncHomeVisitAction> entry : map.entrySet()) {
                        String json = entry.getValue().getJsonPayload();
                        if (StringUtils.isNotBlank(json)) {
                            jsons.put(entry.getKey(), json);
                        }
                    }

                    saveVisit(memberID, getEncounterType(), jsons);
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

        if (baseEvent != null) {
            JsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(baseEvent));
            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson);

            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unsynced));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
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

    public ECSyncHelper getSyncHelper() {
        return AncLibrary.getInstance().getEcSyncHelper();
    }

    public ClientProcessorForJava getClientProcessorForJava() {
        return AncLibrary.getInstance().getClientProcessorForJava();
    }
}
