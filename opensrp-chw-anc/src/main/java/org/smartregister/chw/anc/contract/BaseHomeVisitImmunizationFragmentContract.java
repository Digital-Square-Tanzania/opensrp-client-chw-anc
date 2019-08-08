package org.smartregister.chw.anc.contract;

import org.json.JSONObject;

import java.util.Map;

public interface BaseHomeVisitImmunizationFragmentContract {

    interface View {
        void initializePresenter();

        void updateNoVaccineCheck(boolean state);

        void updateSelectedVaccines(Map<String, String> selectedVaccines, boolean variedMode);

        JSONObject getJsonObject();
    }

    interface Presenter {

        /**
         * notifies the view if no vaccine was selected
         *
         * @param state
         */
        void onNoVaccineStatus(boolean state);

        /**
         * Returns a map of the vaccine and the current value
         *
         * @param selectedVaccines
         */
        void onSelectedVaccinesInitialized(Map<String, String> selectedVaccines, boolean variedMode);
    }

    interface Model {
        void getVaccinesState(JSONObject jsonObject, Presenter presenter);
    }
}
