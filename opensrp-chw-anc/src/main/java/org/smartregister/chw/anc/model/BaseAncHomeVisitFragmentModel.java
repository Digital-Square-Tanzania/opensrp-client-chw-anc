package org.smartregister.chw.anc.model;

import android.content.res.Resources;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.opensrp_chw_anc.R;

import timber.log.Timber;

public class BaseAncHomeVisitFragmentModel implements BaseAncHomeVisitFragmentContract.Model {


    @Override
    public void processJson(JSONObject jsonObject, BaseAncHomeVisitFragmentContract.Presenter presenter) {
        if (jsonObject != null) {

            // extract title , question and view type and notify the view
            String title = getTitle(jsonObject);
            String question = getQuestion(jsonObject);
            int image = getImage(jsonObject, presenter);
            BaseAncHomeVisitFragment.QuestionType questionType = getQuestionType(jsonObject);
            String value = getValue(jsonObject);

            presenter.setTitle(title);
            presenter.setQuestion(question);
            presenter.setImageRes(image);
            presenter.setQuestionType(questionType);
            presenter.setValue(value);

        }
    }

    @Override
    public void writeValue(JSONObject jsonObject, String value) {
        if (jsonObject != null) {
            try {
                jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).put(JsonFormConstants.VALUE, value);
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }


    private String getTitle(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(JsonFormConstants.STEP1).getString(JsonFormConstants.STEP_TITLE);
        } catch (JSONException e) {
            Timber.e(e);
            return "";
        }
    }

    private String getQuestion(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.HINT);
        } catch (JSONException e) {
            Timber.e(e);
            return "";
        }
    }

    private int getImage(JSONObject jsonObject, BaseAncHomeVisitFragmentContract.Presenter presenter) {
        try {
            String image = jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormUtils.IMAGE);
            if (presenter.getView() != null) {
                Resources resources = presenter.getView().getMyContext().getResources();
                return resources.getIdentifier(image, "drawable",
                        presenter.getView().getMyContext().getPackageName());
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
        return R.drawable.badge;
    }

    private BaseAncHomeVisitFragment.QuestionType getQuestionType(JSONObject jsonObject) {
        try {
            String type = jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.TYPE);
            switch (type) {
                case JsonFormConstants.SPINNER:
                    return BaseAncHomeVisitFragment.QuestionType.BOOLEAN;
                case JsonFormConstants.CHECK_BOX:
                    return BaseAncHomeVisitFragment.QuestionType.MULTI_OPTIONS;
                case JsonFormConstants.DATE_PICKER:
                    return BaseAncHomeVisitFragment.QuestionType.DATE_SELECTOR;
                default:
                    return BaseAncHomeVisitFragment.QuestionType.BOOLEAN;
            }
        } catch (JSONException e) {
            Timber.e(e);
            return BaseAncHomeVisitFragment.QuestionType.BOOLEAN;
        }
    }

    private String getValue(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.VALUE);
        } catch (JSONException e) {
            return "";
        }
    }
}
