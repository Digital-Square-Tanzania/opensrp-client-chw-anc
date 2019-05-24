package org.smartregister.chw.anc.contract;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

public interface AncRegisterContract {

    interface View extends BaseRegisterContract.View {

        Presenter presenter();

        Form getFormConfig();

        void startFormActivity(String formName, String memberID, String entityId, String metaData);
    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void startForm(String formName, String memberID,  String entityId, String metadata, String currentLocationId) throws Exception;

        void saveForm(String jsonString, boolean isEditMode);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String memberID,  String entityId,
                                 String currentLocationId) throws Exception;

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void saveRegistration(String jsonString, boolean isEditMode, final InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved(boolean isEdit);

    }
}