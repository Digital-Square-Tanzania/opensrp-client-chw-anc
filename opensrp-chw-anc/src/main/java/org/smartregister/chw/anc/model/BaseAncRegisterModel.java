package org.smartregister.chw.anc.model;

import org.json.JSONObject;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.contract.AncRegisterContract;
import org.smartregister.chw.anc.interactor.JsonFormUtils;
import org.smartregister.util.FormUtils;


public class BaseAncRegisterModel implements AncRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = FormUtils.getInstance(AncLibrary.getInstance().context().applicationContext()).getFormJson(formName);

        JsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }
}
