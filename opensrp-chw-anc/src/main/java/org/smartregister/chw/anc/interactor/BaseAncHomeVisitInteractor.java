package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.util.Utils;

import timber.log.Timber;

import static org.smartregister.util.Utils.getName;

public class BaseAncHomeVisitInteractor implements BaseAncHomeVisitContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BaseAncHomeVisitInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAncHomeVisitInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(String jsonString, boolean isEditMode, BaseAncHomeVisitContract.InteractorCallBack callBack) {

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

    public String getTableName() {
        return Constants.TABLES.FAMILY_MEMBER;
    }

    public CommonRepository getCommonRepository(String tableName) {
        return AncLibrary.getInstance().context().commonrepository(tableName);
    }
}
