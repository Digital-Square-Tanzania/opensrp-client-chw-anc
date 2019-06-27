package org.smartregister.chw.anc.contract;

import android.content.Context;

import org.smartregister.chw.anc.model.BaseHomeVisitHistory;

import java.util.List;

public interface BaseAncMedicalHistoryContract {

    interface View {

        void initializePresenter();

        Presenter getPresenter();

        void onDataReceived(List<BaseHomeVisitHistory> historyList);

        Context getViewContext();
    }

    interface Presenter {

        void initialize();

        View getView();
    }

    interface Interactor {

        void getMemberHistory(String memberID, Context context, InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onDataFetched(List<BaseHomeVisitHistory> historyList);

    }

}
