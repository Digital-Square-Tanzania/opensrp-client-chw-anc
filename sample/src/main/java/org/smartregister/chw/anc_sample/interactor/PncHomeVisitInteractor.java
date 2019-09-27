package org.smartregister.chw.anc_sample.interactor;

import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncHomeVisitInteractor;
import org.smartregister.chw.anc_sample.activity.EntryActivity;

public class PncHomeVisitInteractor extends BaseAncHomeVisitInteractor {

    @Override
    public MemberObject getMemberClient(String memberID) {
        return EntryActivity.getSampleMember();
    }
}
