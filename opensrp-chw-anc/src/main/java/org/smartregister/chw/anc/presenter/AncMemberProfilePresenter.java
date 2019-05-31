package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.interactor.AncMemberProfileInteractor;
import org.smartregister.chw.anc.util.MemberObject;
import org.smartregister.view.contract.BaseProfileContract;

import java.lang.ref.WeakReference;

public class AncMemberProfilePresenter implements BaseProfileContract.Presenter, AncMemberProfileContract.InteractorCallBack, AncMemberProfileContract.Presenter {

    private static final String TAG = AncMemberProfilePresenter.class.getCanonicalName();

    private WeakReference<AncMemberProfileContract.View> view;
    private AncMemberProfileContract.Interactor interactor;

    private MemberObject memberObject;

    public AncMemberProfilePresenter(AncMemberProfileContract.View view, MemberObject memberObject) {
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

    }
}
