package org.smartregister.chw.pnc.interactor;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.VisitUtils;

import java.util.List;

public class BasePncMedicalHistoryInteractor implements BaseAncMedicalHistoryContract.Interactor {

    protected AppExecutors appExecutors;

    @VisibleForTesting
    BasePncMedicalHistoryInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BasePncMedicalHistoryInteractor() {
        this(new AppExecutors());
    }

    public void getMemberHistory(final String memberID, final Context context, final BaseAncMedicalHistoryContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            final List<Visit> visits = VisitUtils.getVisits(memberID, Constants.EVENT_TYPE.PNC_HOME_VISIT);
            appExecutors.mainThread().execute(() -> callBack.onDataFetched(visits));
        };

        appExecutors.diskIO().execute(runnable);
    }


}
