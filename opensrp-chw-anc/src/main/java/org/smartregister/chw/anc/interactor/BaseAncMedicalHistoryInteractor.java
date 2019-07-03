package org.smartregister.chw.anc.interactor;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final List<Visit> visits = getVisits(memberID);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDataFetched(visits);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    private List<Visit> getVisits(String memberID) {

        List<Visit> visits = new ArrayList<>(AncLibrary.getInstance().visitRepository().getVisits(memberID, Constants.EVENT_TYPE.ANC_HOME_VISIT));

        int x = 0;
        while (visits.size() > x) {
            Visit visit = visits.get(x);
            List<VisitDetail> detailList = AncLibrary.getInstance().visitDetailsRepository().getVisits(visit.getVisitId());
            visits.get(x).setVisitDetails(getVisitGroups(detailList));
            x++;
        }

        return visits;
    }

    private Map<String, List<VisitDetail>> getVisitGroups(List<VisitDetail> detailList) {
        Map<String, List<VisitDetail>> visitMap = new HashMap<>();

        for (VisitDetail visitDetail : detailList) {

            List<VisitDetail> visitDetailList = visitMap.get(visitDetail.getVisitKey());
            if (visitDetailList == null)
                visitDetailList = new ArrayList<>();

            visitDetailList.add(visitDetail);

            visitMap.put(visitDetail.getVisitKey(), visitDetailList);
        }

        return visitMap;
    }

}
