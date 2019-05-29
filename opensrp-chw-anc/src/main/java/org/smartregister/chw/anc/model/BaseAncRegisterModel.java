package org.smartregister.chw.anc.model;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.AncRegisterContract;
import org.smartregister.chw.anc.interactor.JsonFormUtils;


public class BaseAncRegisterModel implements AncRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);
        JsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }
}
