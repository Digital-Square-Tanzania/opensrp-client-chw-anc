package org.smartregister.chw.pnc.presenter;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.pnc.contract.BasePncRegisterContract;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.util.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;

public class BasePncRegisterPresenter implements BasePncRegisterContract.Presenter, BasePncRegisterContract.InteractorCallBack{

    protected WeakReference<BasePncRegisterContract.View> viewReference;
    protected BasePncRegisterContract.Interactor interactor;
    protected BasePncRegisterContract.Model model;

    public BasePncRegisterPresenter(BasePncRegisterContract.View view, BasePncRegisterContract.Model model, BasePncRegisterContract.Interactor interactor) {
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

     @Override
    public void saveForm(String jsonString, boolean isEditMode, String table) {
        try {

            getView().showProgressDialog(org.smartregister.chw.opensrp_chw_anc.R.string.saving_dialog_title);
            interactor.saveRegistration(jsonString, isEditMode, this, table);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        if (getView() != null) {
            getView().onRegistrationSaved(isEdit);
            getView().hideProgressDialog();
        }
    }

    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
        if (viewIdentifiers != null)
            ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().registerViewConfigurations(viewIdentifiers);
    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
        if (viewIdentifiers != null)
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

    private BasePncRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

}
