package org.smartregister.chw.anc.contract;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

public interface AncRegisterContract {

    interface View extends BaseRegisterContract.View {

        Presenter presenter();

        Form getFormConfig();

    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        void saveForm(String jsonString, boolean isEditMode);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId,
                                 String currentLocationId) throws Exception;

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void saveRegistration(final InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved(boolean isEdit);

    }
}