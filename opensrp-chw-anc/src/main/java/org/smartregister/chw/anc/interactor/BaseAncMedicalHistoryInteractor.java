package org.smartregister.chw.anc.interactor;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.VisitUtils;

import java.util.List;

public class BaseAncMedicalHistoryInteractor implements BaseAncMedicalHistoryContract.Interactor {

    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseAncMedicalHistoryInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAncMedicalHistoryInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void getMemberHistory(final String memberID, final Context context, final BaseAncMedicalHistoryContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {

            final List<Visit> visits = VisitUtils.getVisits(memberID);
            appExecutors.mainThread().execute(() -> callBack.onDataFetched(visits));
        };

        appExecutors.diskIO().execute(runnable);
    }

}
