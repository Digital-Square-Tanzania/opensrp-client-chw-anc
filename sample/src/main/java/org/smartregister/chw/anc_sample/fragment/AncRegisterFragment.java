package org.smartregister.chw.anc_sample.fragment;

import org.smartregister.chw.anc.fragment.BaseAncRegisterFragment;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc_sample.activity.AncHomeVisitActivity;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public class AncRegisterFragment extends BaseAncRegisterFragment {

    @Override
    protected void openHomeVisit(CommonPersonObjectClient client) {
        String baseEntityId = org.smartregister.util.Utils.getValue(client.getColumnmaps(), DBConstants.KEY.BASE_ENTITY_ID, true);
        AncHomeVisitActivity.startMe(getActivity(), baseEntityId);
    }
}
