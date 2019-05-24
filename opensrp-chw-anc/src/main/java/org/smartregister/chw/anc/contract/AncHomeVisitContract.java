package org.smartregister.chw.anc.contract;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

public class AncHomeVisitContract {

    interface View extends BaseRegisterContract.View {

        AncHomeVisitContract.Presenter presenter();

        Form getFormConfig();

        void start();
    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void startForm(String formName, String entityId, String metadata, String currentLocationId);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void saveRegistration(String jsonString, boolean isEditMode, final AncRegisterContract.InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved(boolean isEdit);

    }
}
