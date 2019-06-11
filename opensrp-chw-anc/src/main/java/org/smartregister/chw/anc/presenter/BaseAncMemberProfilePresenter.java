package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.interactor.AncMemberProfileInteractor;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.view.contract.BaseProfileContract;

import java.lang.ref.WeakReference;

public class BaseAncMemberProfilePresenter implements BaseProfileContract.Presenter, AncMemberProfileContract.InteractorCallBack, AncMemberProfileContract.Presenter {

    protected WeakReference<AncMemberProfileContract.View> view;
    private AncMemberProfileContract.Interactor interactor;

    private MemberObject memberObject;

    public BaseAncMemberProfilePresenter(AncMemberProfileContract.View view, MemberObject memberObject) {
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
        getView().setMemberGA(memberObject.getLastMenstrualPeriod());
        getView().setMemberAddress(memberObject.getAddress());
        getView().setMemberChwMemberId(memberObject.getChwMemberId());
    }

    @Override
    public AncMemberProfileContract.View getView() {
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
