package org.smartregister.chw.anc.interactor;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.model.BaseHomeVisitHistory;
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
    public void getMemberHistory(String memberID, Context context, final BaseAncMedicalHistoryContract.InteractorCallBack callBack) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<BaseHomeVisitHistory> actions = new ArrayList<>();
                actions.add(new BaseHomeVisitHistory("LAST VISIT", new ArrayList<String>()));
                actions.add(new BaseHomeVisitHistory("ANC CARD", new ArrayList<String>()));
                actions.add(new BaseHomeVisitHistory("ANC HEALTH FACILITY VISITS", new ArrayList<String>()));
                actions.add(new BaseHomeVisitHistory("TT IMMUNIZATIONS", new ArrayList<String>()));
                actions.add(new BaseHomeVisitHistory("IPTP-SP DOSES", new ArrayList<String>()));

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDataFetched(actions);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    protected List<Visit> getVisists(String memberID) {

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

    protected Map<String, List<VisitDetail>> getVisitGroups(List<VisitDetail> detailList) {
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
