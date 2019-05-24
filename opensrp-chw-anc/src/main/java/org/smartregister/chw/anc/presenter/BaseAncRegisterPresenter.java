package org.smartregister.chw.anc.presenter;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.AncRegisterContract;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.util.Utils;

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
        if (StringUtils.isBlank(entityId)) {
            return;
        }

        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
        getView().startFormActivity(form);
    }

    /**
     * Override this method to provide customizations for form editing
     *
     * @param jsonString
     * @param isEditMode
     */
    @Override
    public void saveForm(String jsonString, boolean isEditMode) {
        try {

            getView().showProgressDialog(R.string.saving_dialog_title);
            interactor.saveRegistration(jsonString, isEditMode, this);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        getView().hideProgressDialog();
    }

    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
        ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().registerViewConfigurations(viewIdentifiers);
    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
        ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().unregisterViewConfiguration(viewIdentifiers);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        viewReference = null;//set to null on destroy
        // Inform interactor
        interactor.onDestroy(isChangingConfiguration);
        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            interactor = null;
            model = null;
        }
    }

    @Override
    public void updateInitials() {
        String initials = Utils.getUserInitials();
        if (initials != null && getView() != null) {
            getView().updateInitialsText(initials);
        }
    }

    private AncRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }
}
