package org.smartregister.chw.pnc.contract;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

public interface BasePncRegisterContract {

    interface View extends BaseRegisterContract.View {

        BasePncRegisterContract.Presenter presenter();

        Form getFormConfig();

        void onRegistrationSaved(boolean isEdit);

    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        void saveForm(String jsonString, boolean isEditMode, String table);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId,
                                 String currentLocationId) throws Exception;

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void saveRegistration(String jsonString, boolean isEditMode, final BasePncRegisterContract.InteractorCallBack callBack, String table);

    }

    interface InteractorCallBack {

        void onRegistrationSaved(boolean isEdit);

    }
}

