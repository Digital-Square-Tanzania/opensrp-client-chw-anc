package org.smartregister.chw.anc.contract;

import android.content.Context;

import org.ei.drishti.dto.AlertStatus;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.view.contract.BaseProfileContract;

import java.util.Date;

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

        void showProgressBar(boolean status);

        void setLastVisit(Date lastVisitDate);

        void setUpComingServicesStatus(String service, AlertStatus status, Date date);

        void setFamilyStatus(AlertStatus status);
    }

    interface Presenter extends BaseProfileContract.Presenter {

        BaseAncMemberProfileContract.View getView();

        void fetchProfileData();

        void refreshProfileBottom();
    }

    interface Interactor {

        void refreshProfileView(MemberObject memberObject, boolean isForEdit, BaseAncMemberProfileContract.InteractorCallBack callback);

        void refreshProfileInfo(String memberID, BaseAncMemberProfileContract.InteractorCallBack callback);
    }

    interface InteractorCallBack {

        void refreshProfileTopSection(MemberObject memberObject);

        void refreshLastVisit(Date lastVisitDate);

        void refreshUpComingServicesStatus(String service, AlertStatus status, Date date);

        void refreshFamilyStatus(AlertStatus status);
    }


}
