package org.smartregister.chw.pnc.contract;

import android.content.Context;

import org.smartregister.chw.anc.domain.GroupedVisit;
import java.util.List;

public interface BasePncMedicalHistoryContract {

    interface View {
        void initializePncPresenter();

        Presenter getPncPresenter();

        void onGroupedDataReceived(List<GroupedVisit> groupedVisits);

        Context getViewContext();

        void displayLoadingState(boolean state);

        android.view.View renderMedicalHistoryView(List<GroupedVisit> groupedVisits);
    }

    interface Presenter {

        void initialize();

        View getView();
    }

    interface Interactor {
        void getMemberHistory(String memberID, Context context, InteractorCallBack callBack);
    }

    interface InteractorCallBack {
        void onDataFetched(List<GroupedVisit> groupedVisits);
    }
}
