package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.util.AppExecutors;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AncMemberProfileInteractor implements BaseAncMemberProfileContract.Interactor {
    public static final String TAG = AncMemberProfileInteractor.class.getName();
    private AppExecutors appExecutors;
    private Map<String, Date> vaccineList = new LinkedHashMap<>();

    @VisibleForTesting
    AncMemberProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public AncMemberProfileInteractor() {
        this(new AppExecutors());
    }

    public Map<String, Date> getVaccineList() {
        return vaccineList;
    }

    @Override
    public void refreshProfileView(final MemberObject memberObject, final boolean isForEdit, final BaseAncMemberProfileContract.InteractorCallBack callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.refreshProfileTopSection(memberObject);
                    }
                });
            }
        };
        appExecutors.diskIO().execute(runnable);
    }


    @Override
    public void updateVisitNotDone(long value, BaseAncMemberProfileContract.InteractorCallBack callback) {

//       Implement
    }


}
