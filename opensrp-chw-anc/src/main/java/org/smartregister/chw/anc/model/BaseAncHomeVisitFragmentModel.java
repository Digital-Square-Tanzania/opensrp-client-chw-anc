package org.smartregister.chw.anc.model;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;

public class BaseAncHomeVisitFragmentModel implements BaseAncHomeVisitFragmentContract.Model {


    @Override
    public void processJson(JSONObject jsonObject, BaseAncHomeVisitFragmentContract.Presenter presenter) {
        if (jsonObject != null) {

            try {
                // extract title , question and view type and notify the view
                String title = jsonObject.getJSONObject(JsonFormConstants.STEP1).getString(JsonFormConstants.STEP_TITLE);

                if (presenter != null) {
                    presenter.setTitle(title);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void writeValue(JSONObject jsonObject, String key, String value) {

    }
}
