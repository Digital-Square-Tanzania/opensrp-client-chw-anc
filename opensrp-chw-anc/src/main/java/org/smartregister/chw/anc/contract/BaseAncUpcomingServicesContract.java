package org.smartregister.chw.anc.contract;

import org.smartregister.chw.anc.model.BaseUpcomingService;

import java.util.List;

public interface BaseAncUpcomingServicesContract {

    interface View {

        void initializePresenter();

        Presenter getPresenter();

        void displayLoadingState(boolean state);

        void refreshServices(List<BaseUpcomingService> serviceList);
    }

    interface Presenter {

        void initialize();

        View getView();
    }

    interface Interactor {

        void getUpComingServices(String memberID, InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onDataFetched(List<BaseUpcomingService> serviceList);

    }
}
