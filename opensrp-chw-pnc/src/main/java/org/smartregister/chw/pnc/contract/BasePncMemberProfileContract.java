package org.smartregister.chw.pnc.contract;

import android.widget.TextView;

import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;

public interface BasePncMemberProfileContract extends BaseAncMemberProfileContract {

    interface Interactor {
        String getPncMotherNameDetails(MemberObject memberObject, TextView textView);
    }

}
