package org.smartregister.chw.anc.model;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.AncRegisterContract;

public class BaseAncRegisterModel implements AncRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        return null;
    }

}
