package org.smartregister.chw.anc.presenter;

import org.ei.drishti.dto.AlertStatus;
import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.view.contract.BaseProfileContract;

import java.lang.ref.WeakReference;
import java.util.Date;

public class BaseAncMemberProfilePresenter implements BaseProfileContract.Presenter, BaseAncMemberProfileContract.InteractorCallBack, BaseAncMemberProfileContract.Presenter {

    protected WeakReference<BaseAncMemberProfileContract.View> view;
    protected BaseAncMemberProfileContract.Interactor interactor;

    private MemberObject memberObject;

    public BaseAncMemberProfilePresenter(BaseAncMemberProfileContract.View view, BaseAncMemberProfileContract.Interactor interactor, MemberObject memberObject) {
        this.view = new WeakReference<>(view);
        this.interactor = interactor;
        this.memberObject = memberObject;
    }

    @Override
    public void fetchProfileData() {
        interactor.refreshProfileView(memberObject, false, this);
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo(memberObject, this);
    }

    @Override
    public void refreshProfileTopSection(MemberObject memberObject) {
        String entityType = memberObject.getBaseEntityId();
        getView().setMemberName(memberObject.getMemberName());
        getView().setMemberGA(String.valueOf(memberObject.getGestationAge()));
        getView().setMemberAddress(memberObject.getAddress());
        getView().setMemberChwMemberId(memberObject.getChwMemberId());
        getView().setProfileImage(memberObject.getBaseEntityId(), entityType);
    }

    @Override
    public void refreshLastVisit(Date lastVisitDate) {
        if (getView() != null) {
            getView().setLastVisit(lastVisitDate);
        }
    }

    @Override
    public void refreshUpComingServicesStatus(String service, AlertStatus status, Date date) {
        if (getView() != null) {
            getView().setUpComingServicesStatus(service, status, date);
            getView().showProgressBar(false);
        }
    }

    @Override
    public void refreshFamilyStatus(AlertStatus status) {
        if (getView() != null) {
            getView().setFamilyStatus(status);
        }
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
