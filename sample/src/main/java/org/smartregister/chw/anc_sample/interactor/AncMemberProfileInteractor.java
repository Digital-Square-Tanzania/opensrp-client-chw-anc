package org.smartregister.chw.anc_sample.interactor;

import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncMemberProfileInteractor;
import org.smartregister.chw.anc_sample.activity.EntryActivity;

public class AncMemberProfileInteractor extends BaseAncMemberProfileInteractor {

    @Override
    public MemberObject getMemberClient(String memberID) {
        return EntryActivity.getSampleMember();
    }
}
