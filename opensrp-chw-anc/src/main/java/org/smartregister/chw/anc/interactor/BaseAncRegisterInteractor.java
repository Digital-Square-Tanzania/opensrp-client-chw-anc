package org.smartregister.chw.anc.interactor;

import org.smartregister.chw.anc.contract.AncRegisterContract;

public class BaseAncRegisterInteractor implements AncRegisterContract.Interactor  {
    @Override
    public void onDestroy(boolean isChangingConfiguration) {

    }

    @Override
    public void saveRegistration(String jsonString, boolean isEditMode, AncRegisterContract.InteractorCallBack callBack) {
        // save it

    }
}
