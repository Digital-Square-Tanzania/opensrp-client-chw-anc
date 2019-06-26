package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.AncMemberProfileInteractor;
import org.smartregister.view.contract.BaseProfileContract;

import java.lang.ref.WeakReference;

public class BaseAncMemberProfilePresenter implements BaseProfileContract.Presenter, BaseAncMemberProfileContract.InteractorCallBack, BaseAncMemberProfileContract.Presenter {

    protected WeakReference<BaseAncMemberProfileContract.View> view;
    private BaseAncMemberProfileContract.Interactor interactor;

    private MemberObject memberObject;

    public BaseAncMemberProfilePresenter(BaseAncMemberProfileContract.View view, MemberObject memberObject) {
        this.view = new WeakReference<>(view);
        this.interactor = new AncMemberProfileInteractor();
        this.memberObject = memberObject;
    }

    @Override
    public void fetchProfileData() {
        interactor.refreshProfileView(memberObject, false, this);
    }

    @Override
    public void refreshProfileTopSection(MemberObject memberObject) {
        getView().setMemberName(memberObject.getMemberName());
        getView().setMemberGA(String.valueOf(memberObject.getGestationAge()));
        getView().setMemberAddress(memberObject.getAddress());
        getView().setMemberChwMemberId(memberObject.getChwMemberId());
    }

    @Override
    public BaseAncMemberProfileContract.View getView() {
        if (view != null) {
            return view.get();
        } else {
            return null;
        }
    }


    @Override
    public void onDestroy(boolean b) {
//        TODO implement onDestroy
    }
}
