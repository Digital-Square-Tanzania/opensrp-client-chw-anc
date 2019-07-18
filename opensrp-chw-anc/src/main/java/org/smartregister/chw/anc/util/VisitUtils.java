package org.smartregister.chw.anc.util;

import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitUtils {

    public static List<Visit> getVisits(String memberID) {

        List<Visit> visits = getVisitsOnly(memberID);

        int x = 0;
        while (visits.size() > x) {
            Visit visit = visits.get(x);
            List<VisitDetail> detailList = getVisitDetailsOnly(visit.getVisitId());
            visits.get(x).setVisitDetails(getVisitGroups(detailList));
            x++;
        }

        return visits;
    }

    public static List<Visit> getVisitsOnly(String memberID) {
        return new ArrayList<>(AncLibrary.getInstance().visitRepository().getVisits(memberID, Constants.EVENT_TYPE.ANC_HOME_VISIT));
    }

    public static List<VisitDetail> getVisitDetailsOnly(String visitID) {
        return AncLibrary.getInstance().visitDetailsRepository().getVisits(visitID);
    }

    public static Map<String, List<VisitDetail>> getVisitGroups(List<VisitDetail> detailList) {
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
