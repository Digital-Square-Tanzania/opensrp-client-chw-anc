package org.smartregister.chw.anc.presenter;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.chw.anc.contract.AncRegisterContract;

import java.lang.ref.WeakReference;
import java.util.List;

public class BaseAncRegisterPresenter implements AncRegisterContract.Presenter, AncRegisterContract.InteractorCallBack {

    public static final String TAG = BaseAncRegisterPresenter.class.getName();

    protected WeakReference<AncRegisterContract.View> viewReference;
    protected AncRegisterContract.Interactor interactor;
    protected AncRegisterContract.Model model;

    public BaseAncRegisterPresenter(AncRegisterContract.View view, AncRegisterContract.Model model, AncRegisterContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.model = model;
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {

    }

    @Override
    public void saveForm(String jsonString, boolean isEditMode) {

    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {

    }

    @Override
    public void onNoUniqueId() {

    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {

    }

    @Override
    public void registerViewConfigurations(List<String> list) {

    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {

    }

    @Override
    public void onDestroy(boolean b) {

    }

    @Override
    public void updateInitials() {

    }
}
