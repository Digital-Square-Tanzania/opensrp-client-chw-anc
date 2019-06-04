package org.smartregister.chw.anc.contract;

import android.content.Context;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BaseAncHomeVisitContract {

    interface View {

        BaseAncHomeVisitContract.Presenter presenter();

        Form getFormConfig();

        void startForm(BaseAncHomeVisitAction ancHomeVisitAction);

        void startFormActivity(JSONObject jsonForm);

        void startFragment(BaseAncHomeVisitAction ancHomeVisitAction);

        void redrawHeader(String memberName, String age);

        void redrawVisitUI();

        void displayProgressBar(boolean state);

        Map<String, BaseAncHomeVisitAction> getAncHomeVisitActions();

        void close();

        Presenter getPresenter();

        /**
         * Save the received data into the events table
         * Start aggregation of all events and persist results into the events table
         */
        void submitVisit();

        /**
         * Results action when a dialog is opened and returns a payload
         *
         * @param jsonString
         */
        void onDialogOptionUpdated(String jsonString);

        void initializeActions(LinkedHashMap<String, BaseAncHomeVisitAction> map);

        Context getContext();

        void displayToast(String message);
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

        void submitVisit();
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface Interactor {

        void saveRegistration(String jsonString, boolean isEditMode, final BaseAncHomeVisitContract.InteractorCallBack callBack);

        void getUserInformation(String memberID, final BaseAncHomeVisitContract.InteractorCallBack callBack);

        void calculateActions(View view, String memberID, BaseAncHomeVisitContract.InteractorCallBack callBack);

        void submitVisit(String memberID, Map<String, BaseAncHomeVisitAction> map, InteractorCallBack callBack);
    }

    interface InteractorCallBack {

        void onRegistrationSaved(boolean isEdit);

        void onMemberDetailsLoaded(String memberName, String age);

        void preloadActions(LinkedHashMap<String, BaseAncHomeVisitAction> map);

        void onSubmitted(boolean successful);
    }
}
