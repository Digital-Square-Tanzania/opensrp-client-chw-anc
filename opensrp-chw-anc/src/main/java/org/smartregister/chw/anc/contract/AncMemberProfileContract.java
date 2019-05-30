package org.smartregister.chw.anc.contract;

import android.content.Context;

import org.smartregister.chw.anc.util.MemberObject;
import org.smartregister.view.contract.BaseProfileContract;

public interface AncMemberProfileContract {

    interface View {
        Context getContext();

        void setMemberName(String memberName);

        AncMemberProfileContract.Presenter presenter();

    }

    interface Presenter extends BaseProfileContract.Presenter {

        AncMemberProfileContract.View getView();

        void fetchProfileData();

    }

    interface Interactor {

        void refreshProfileView(MemberObject memberObject, boolean isForEdit, AncMemberProfileContract.InteractorCallBack callback);
    }

    interface InteractorCallBack {

        void refreshProfileTopSection(MemberObject memberObject);

    }


}
