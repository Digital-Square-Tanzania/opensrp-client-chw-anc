package org.smartregister.chw.anc.contract;

import android.support.v4.app.Fragment;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;

import java.util.Map;

public interface BaseAncHomeVisitContract {

    interface View {

        BaseAncHomeVisitContract.Presenter presenter();

        Form getFormConfig();

        void startFrom(String formName);

        void startFragment(Fragment fragment);

        void redrawHeader(String memberName, String age);

        void redrawVisitUI();

        void displayProgressBar(boolean state);

        Map<String, BaseAncHomeVisitAction> getAncHomeVisitActions();

        void close();

        Presenter getPresenter();

        void submitVisit();

    }

    interface Presenter {

        void startForm(String formName, String memberID, String currentLocationId);

        /**
         * Recall this method to redraw ui after every submission
         *
         * @return
         */
        boolean validateStatus();

        /**
         * Preload header and visit
         */
        void initialize();

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface Interactor {

        void saveRegistration(String jsonString, boolean isEditMode, final BaseAncHomeVisitContract.InteractorCallBack callBack);

        void getUserInformation(String memberID, final BaseAncHomeVisitContract.InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved(boolean isEdit);

        void onMemberDetailsLoaded(String memberName, String age);

    }
}
