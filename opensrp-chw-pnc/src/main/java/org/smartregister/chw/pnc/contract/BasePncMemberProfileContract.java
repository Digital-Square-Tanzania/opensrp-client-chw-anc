package org.smartregister.chw.pnc.contract;

import android.util.Pair;
import android.widget.TextView;

import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public interface BasePncMemberProfileContract extends BaseAncMemberProfileContract {

    interface Interactor {
        String getPncMotherNameDetails(MemberObject memberObject, TextView textView, CircleImageView imageView);

        String getPncDay(String motherBaseID);

        List<CommonPersonObjectClient> pncChildrenUnder29Days(String motherBaseID);

        void updateChild(final Pair<Client, Event> pair, final String jsonString);
    }

}
