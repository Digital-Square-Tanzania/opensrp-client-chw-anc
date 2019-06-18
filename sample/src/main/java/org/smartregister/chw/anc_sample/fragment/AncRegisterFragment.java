package org.smartregister.chw.anc_sample.fragment;

import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.fragment.BaseAncRegisterFragment;
import org.smartregister.chw.anc_sample.activity.AncHomeVisitActivity;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public class AncRegisterFragment extends BaseAncRegisterFragment {

    @Override
    protected void openHomeVisit(CommonPersonObjectClient client) {
        AncHomeVisitActivity.startMe(getActivity(), new MemberObject(client));
    }
}
