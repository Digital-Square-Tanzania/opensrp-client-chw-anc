package org.smartregister.chw.pnc.contract;

import android.widget.TextView;

import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public interface BasePncMemberProfileContract extends BaseAncMemberProfileContract {

    interface Interactor {
        String getPncMotherNameDetails(MemberObject memberObject, TextView textView, CircleImageView imageView);

        String getPncDay(String motherBaseID);

        List<CommonPersonObjectClient> pncChildrenUnder29Days(String motherBaseID);
    }

}
