package org.smartregister.chw.anc.actionhelper;

import android.content.Context;

import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;

import java.util.List;
import java.util.Map;

public class ANCCounselingHelper implements BaseAncHomeVisitAction.AncHomeVisitActionHelper  {
    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {

    }

    @Override
    public void onPayloadReceived(String jsonPayload) {

    }

    @Override
    public String evaluateSubTitle() {
        return null;
    }

    @Override
    public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
        return null;
    }
}
