package org.smartregister.chw.pnc.interactor;

import org.smartregister.chw.anc.domain.GroupedVisit;
import org.smartregister.chw.pnc.contract.BasePncMedicalHistoryContract;

import java.lang.ref.WeakReference;
import java.util.List;

public class BasePncMedicalHistoryPresenter implements BasePncMedicalHistoryContract.Presenter, BasePncMedicalHistoryContract.InteractorCallBack {

    private BasePncMedicalHistoryContract.Interactor interactor;
    private WeakReference<BasePncMedicalHistoryContract.View> view;
    private String memberID;

    public BasePncMedicalHistoryPresenter(BasePncMedicalHistoryContract.Interactor interactor, BasePncMedicalHistoryContract.View view, String memberID) {
        this.interactor = interactor;
        this.view = new WeakReference<>(view);
        this.memberID = memberID;

        initialize();
    }

    @Override
    public void initialize() {
        if (getView() == null)
            return;

        interactor.getMemberHistory(memberID, getView().getViewContext(), this);
    }

    @Override
    public BasePncMedicalHistoryContract.View getView() {
        if (view.get() != null) {
            return view.get();
        } else {
            return null;
        }
    }

    @Override
    public void onDataFetched(List<GroupedVisit> groupedVisits) {
        if (getView() != null) {
            getView().onGroupedDataReceived(groupedVisits);
        }
    }
}
