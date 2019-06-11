package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.domain.MemberObject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AncMemberProfileInteractor implements AncMemberProfileContract.Interactor {
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
    public void refreshProfileView(final MemberObject memberObject, final boolean isForEdit, final AncMemberProfileContract.InteractorCallBack callback) {
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


}
