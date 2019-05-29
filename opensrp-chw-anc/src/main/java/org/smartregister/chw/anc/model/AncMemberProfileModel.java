package org.smartregister.chw.anc.model;

import android.util.Log;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.interactor.JsonFormUtils;
import org.smartregister.util.FormUtils;

public class AncMemberProfileModel implements AncMemberProfileContract.Model {

    private FormUtils formUtils;
    private String familyName;

    public AncMemberProfileModel(String familyName) {
        this.familyName = familyName;
    }

    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId, String familyID) throws Exception {
        JSONObject form = getFormUtils().getFormJson(formName);
        if (form == null) {
            return null;
        }
        form = JsonFormUtils.getFormAsJson(form, formName, entityId, currentLocationId, familyID);
        if (formName.equals(Constants.JSON_FORM.CHILD_REGISTER)) {
            JsonFormUtils.updateJsonForm(form, familyName);
        }

        return form;
    }


    private FormUtils getFormUtils() {

        if (formUtils == null) {
            try {
                formUtils = FormUtils.getInstance(Utils.context().applicationContext());
            } catch (Exception e) {
                Log.e(ChildRegisterModel.class.getCanonicalName(), e.getMessage(), e);
            }
        }
        return formUtils;
    }
}
