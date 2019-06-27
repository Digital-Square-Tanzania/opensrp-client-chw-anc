package org.smartregister.chw.anc.contract;

import org.smartregister.chw.anc.domain.Visit;

import java.util.List;

public interface BaseAncMedicalHistoryContract {

    interface View {

        void initializePresenter();

        Presenter getPresenter();

    }

    interface Presenter{

        void initialize();

        void onDataFetched(List<Visit> visits);

    }

    interface Model {

    }

    interface Interactor {

        void getMemberHistory(String memberID, Presenter presenter);

    }

}
