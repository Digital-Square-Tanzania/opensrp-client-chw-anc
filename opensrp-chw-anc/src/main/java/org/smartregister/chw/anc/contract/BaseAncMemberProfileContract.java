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

        void openMedicalHistory();

        void openUpcomingService();

        void openFamilyDueServices();

        void setProfileImage(String baseEntityId, String entityType);

        void setVisitNotDoneThisMonth();

        void updateVisitNotDone(long value);
    }

    interface Presenter extends BaseProfileContract.Presenter {

        BaseAncMemberProfileContract.View getView();

        void fetchProfileData();

    }

    interface Interactor {

        void refreshProfileView(MemberObject memberObject, boolean isForEdit, BaseAncMemberProfileContract.InteractorCallBack callback);

        void updateVisitNotDone(long value, BaseAncMemberProfileContract.InteractorCallBack callback);

    }

    interface InteractorCallBack {

        void refreshProfileTopSection(MemberObject memberObject);

    }


}
