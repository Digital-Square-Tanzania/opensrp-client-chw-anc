package org.smartregister.chw.anc.contract;

import android.content.Context;

import org.json.JSONObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.FetchStatus;
import org.smartregister.view.contract.BaseProfileContract;

public interface AncMemberProfileContract {

    interface View extends BaseProfileContract.View {

        Context getApplicationContext();

        Context getContext();

        String getString(int resourceId);

        void startFormActivity(JSONObject form);

        void refreshProfile(final FetchStatus fetchStatus);

        void displayShortToast(int resourceId);

        void setProfileImage(String baseEntityId);

        void setParentName(String parentName);

        void setGender(String gender);

        void setAddress(String address);

        void setId(String id);

        void setProfileName(String fullName);

        void setAge(String age);

        void setVisitButtonDueStatus();

        void setVisitButtonOverdueStatus();

        void setVisitNotDoneThisMonth();

        void setLastVisitRowView(String days);

        void setServiceNameDue(String name, String dueDate);

        void setServiceNameOverDue(String name, String dueDate);

        void setServiceNameUpcoming(String name, String dueDate);

        void setVisitLessTwentyFourView(String monthName);

        void setVisitAboveTwentyFourView();

        void setFamilyHasNothingDue();

        void setFamilyHasServiceDue();

        void setFamilyHasServiceOverdue();

        AncMemberProfileContract.Presenter presenter();

        void updateHasPhone(boolean hasPhone);

        void enableEdit(boolean enable);
        void hideProgressBar();
    }

    interface Presenter extends BaseProfileContract.Presenter {


        AncMemberProfileContract.View getView();

        void fetchProfileData();

        void updateChildCommonPerson(String baseEntityId);

        void updateVisitNotDone(long value);

        void fetchVisitStatus(String baseEntityId);

        void fetchUpcomingServiceAndFamilyDue(String baseEntityId);

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void updateChildCommonPerson(String baseEntityId);

    }



    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId, String familyID) throws Exception;

    }


}
