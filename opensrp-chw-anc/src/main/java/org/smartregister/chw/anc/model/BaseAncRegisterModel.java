package org.smartregister.chw.anc.model;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncRegisterContract;
import org.smartregister.chw.anc.util.JsonFormUtils;


public class BaseAncRegisterModel implements BaseAncRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);
        JsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }
}
