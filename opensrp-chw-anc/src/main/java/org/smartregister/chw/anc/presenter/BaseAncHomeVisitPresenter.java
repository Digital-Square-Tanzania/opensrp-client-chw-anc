package org.smartregister.chw.anc.presenter;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

import timber.log.Timber;

public class BaseAncHomeVisitPresenter implements BaseAncHomeVisitContract.Presenter, BaseAncHomeVisitContract.InteractorCallBack {

    protected WeakReference<BaseAncHomeVisitContract.View> view;
    protected BaseAncHomeVisitContract.Interactor interactor;
    protected String memberID;

    public BaseAncHomeVisitPresenter(String memberID, BaseAncHomeVisitContract.View view, BaseAncHomeVisitContract.Interactor interactor) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.memberID = memberID;

        initialize();
    }

    @Override
    public void startForm(String formName, String memberID, String currentLocationId) {
        try {
            JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);
            JsonFormUtils.getRegistrationForm(jsonObject, memberID, currentLocationId);

            if (view.get() != null) {
                view.get().startFormActivity(jsonObject);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public boolean validateStatus() {
        return false;
    }

    @Override
    public void initialize() {
        view.get().displayProgressBar(true);
        interactor.getUserInformation(memberID, this);
        interactor.calculateActions(view.get(), memberID, this);
    }

    @Override
    public void submitVisit() {
        if (view.get() != null) {
            interactor.submitVisit(memberID, view.get().getAncHomeVisitActions(), this);
        }
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        Timber.v("onRegistrationSaved");
    }

    @Override
    public void onMemberDetailsLoaded(String memberName, String age) {
        if (view.get() != null) {
            view.get().redrawHeader(memberName, age);
            view.get().displayProgressBar(false);
        }
    }

    @Override
    public void preloadActions(LinkedHashMap<String, BaseAncHomeVisitAction> map) {
        if (view.get() != null) {
            view.get().initializeActions(map);
        }
    }

    @Override
    public void onSubmitted(boolean successful) {
        if (view.get() != null) {
            if (successful) {
                view.get().close();
            } else {
                view.get().displayToast(view.get().getContext().getString(R.string.error_unable_save_home_visit));
            }
        }
    }
}
