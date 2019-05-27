package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;

import java.lang.ref.WeakReference;

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
    public void startForm(String formName, String memberID, String metadata, String currentLocationId) {

    }

    @Override
    public boolean validateStatus() {
        return false;
    }

    @Override
    public void initialize() {
        view.get().displayProgressBar(true);
        interactor.getUserInformation(memberID, this);
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {

    }

    @Override
    public void onMemberDetailsLoaded(String memberName, String age) {
        if (view.get() != null) {
            view.get().redrawHeader(memberName, age);
            view.get().displayProgressBar(false);
        }
    }
}
