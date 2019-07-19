package org.smartregister.chw.pnc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.contract.BasePncRegisterContract;
import org.smartregister.chw.pnc.fragment.BasePncRegisterFragment;
import org.smartregister.chw.pnc.presenter.BasePncRegisterPresenter;
import org.smartregister.view.activity.BaseRegisterActivity;

import java.util.Arrays;
import java.util.List;

import interactor.BasePncRegisterInteractor;
import model.BasePncRegisterModel;
import timber.log.Timber;

public class BasePncRegisterActivity extends BaseRegisterActivity implements BasePncRegisterContract.View {

    protected String BASE_ENTITY_ID;
    protected String ACTION;

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        Timber.v("onRegistrationSaved");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BASE_ENTITY_ID = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        ACTION = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.ACTION);
        onStartActivityWithAction();
    }

    protected void onStartActivityWithAction() {
        if (ACTION != null && ACTION.equals(Constants.ACTIVITY_PAYLOAD_TYPE.REGISTRATION)) {
            startFormActivity(getRegistrationForm(), BASE_ENTITY_ID, null);
        }
    }

    public String getRegistrationForm() {
        return Constants.FORMS.ANC_REGISTRATION;
    }

    @Override
    public void startRegistration() {
        startFormActivity(getRegistrationForm(), null, null);
    }


    @Override
    protected void initializePresenter() {
        presenter = new BasePncRegisterPresenter(this, new BasePncRegisterModel(), new BasePncRegisterInteractor());
    }

    @Override
    protected void onActivityResultExtended(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            try {
                String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                String table = data.getStringExtra(Constants.ACTIVITY_PAYLOAD.TABLE_NAME);
                //  Timber.d("JSONResult", jsonString);

                JSONObject form = new JSONObject(jsonString);
                if (form.getString(Constants.ENCOUNTER_TYPE).equals(getRegisterEventType())) {
                    //   presenter().saveForm(jsonString, false, table);
                }
            } catch (Exception e) {
                Timber.e(e);
            }

        }
    }

    public String getRegisterEventType() {
        return Constants.EVENT_TYPE.ANC_REGISTRATION;
    }

    @Override
    protected BasePncRegisterFragment getRegisterFragment() {
        return new BasePncRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }

    @Override
    public Form getFormConfig() {
        return null;
    }

    public Class getPncFormActivity() {
        return BasePncRegisterActivity.class;
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, getPncFormActivity());
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public BasePncRegisterContract.Presenter presenter() {
        return (BasePncRegisterContract.Presenter) presenter;
    }

    protected String getLocationID() {
        return Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        try {
            if (mBaseFragment instanceof BasePncRegisterFragment) {
                presenter().startForm(formName, entityId, metaData, getLocationID());
            }
        } catch (Exception e) {
            Timber.e(e);
            displayToast(getString(R.string.error_unable_to_start_form));
        }
    }


    @Override
    public List<String> getViewIdentifiers() {
        return Arrays.asList(Constants.CONFIGURATION.ANC_REGISTER);
    }


}
