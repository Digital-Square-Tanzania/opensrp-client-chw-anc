package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.util.Utils;

import java.util.LinkedHashMap;

import timber.log.Timber;

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
                    actionList.put("Danger Signs", new BaseAncHomeVisitAction("Danger Signs", "", false, null, Constants.FORMS.ANC_REGISTRATION));
                    actionList.put("ANC Counseling", new BaseAncHomeVisitAction("ANC Counseling", "", false, null, "anc"));
                    BaseAncHomeVisitFragment llitn = BaseAncHomeVisitFragment.getInstance(view, "Sleeping under a LLITN",
                            "Is the woman sleeping under a Long Lasting Insecticide-Treated Net (LLITN)?",
                            R.drawable.avatar_woman,
                            BaseAncHomeVisitFragment.QuestionType.BOOLEAN
                    );
                    actionList.put("Sleeping under a LLITN", new BaseAncHomeVisitAction("Sleeping under a LLITN", "", false, llitn, null));
                    actionList.put("ANC Card Received", new BaseAncHomeVisitAction("ANC Card Received", "", false, null, "anc"));
                    actionList.put("ANC Health Facility Visit 1", new BaseAncHomeVisitAction("ANC Health Facility Visit 1", "", false, null, "anc"));
                    actionList.put("TT Immunization 1", new BaseAncHomeVisitAction("TT Immunization 1", "", false, null, "anc"));
                    actionList.put("IPTp-SP dose 1", new BaseAncHomeVisitAction("IPTp-SP dose 1", "", false, null, "anc"));
                    actionList.put("Observation & Illness", new BaseAncHomeVisitAction("Observation & Illness", "", true, null, "anc"));
                } catch (BaseAncHomeVisitAction.ValidationException e) {
                    e.printStackTrace();
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

    public String getTableName() {
        return Constants.TABLES.FAMILY_MEMBER;
    }

    public CommonRepository getCommonRepository(String tableName) {
        return AncLibrary.getInstance().context().commonrepository(tableName);
    }
}
