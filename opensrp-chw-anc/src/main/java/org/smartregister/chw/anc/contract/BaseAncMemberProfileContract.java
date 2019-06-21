package org.smartregister.chw.anc.contract;

import android.content.Context;

import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.view.contract.BaseProfileContract;

public interface BaseAncMemberProfileContract {

    interface View {
        Context getContext();

        void setMemberName(String memberName);

        void setMemberGA(String memberGA);

        void setMemberAddress(String memberAddress);

        void setMemberChwMemberId(String memberChwMemberId);

        BaseAncMemberProfileContract.Presenter presenter();

    }

    interface Presenter extends BaseProfileContract.Presenter {

        BaseAncMemberProfileContract.View getView();

        void fetchProfileData();

    }

    interface Interactor {

        void refreshProfileView(MemberObject memberObject, boolean isForEdit, BaseAncMemberProfileContract.InteractorCallBack callback);
    }

    interface InteractorCallBack {

        void refreshProfileTopSection(MemberObject memberObject);

    }


}
