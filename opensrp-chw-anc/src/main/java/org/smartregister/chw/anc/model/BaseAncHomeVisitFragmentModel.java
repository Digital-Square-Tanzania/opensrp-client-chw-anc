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

            try {
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
            } catch (JSONException e) {
                Timber.e(e);
            }

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


    private String getTitle(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(JsonFormConstants.STEP1).getString(JsonFormConstants.STEP_TITLE);
    }

    private String getQuestion(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.HINT);
    }

    private int getImage(JSONObject jsonObject, BaseAncHomeVisitFragmentContract.Presenter presenter) throws JSONException {
        // default image
        int res = R.drawable.badge;
        String image = jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormUtils.IMAGE);
        if (presenter.getView() != null) {
            Resources resources = presenter.getView().getMyContext().getResources();
            res = resources.getIdentifier(image, "drawable",
                    presenter.getView().getMyContext().getPackageName());
        }
        return res;
    }

    private BaseAncHomeVisitFragment.QuestionType getQuestionType(JSONObject jsonObject) throws JSONException {
        BaseAncHomeVisitFragment.QuestionType questionType;
        String type = jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.TYPE);
        switch (type) {
            case JsonFormConstants.SPINNER:
                questionType = BaseAncHomeVisitFragment.QuestionType.BOOLEAN;
                break;
            case JsonFormConstants.CHECK_BOX:
                questionType = BaseAncHomeVisitFragment.QuestionType.MULTI_OPTIONS;
                break;
            case JsonFormConstants.DATE_PICKER:
                questionType = BaseAncHomeVisitFragment.QuestionType.DATE_SELECTOR;
                break;
            default:
                questionType = BaseAncHomeVisitFragment.QuestionType.BOOLEAN;
                break;
        }

        return questionType;
    }

    private String getValue(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.VALUE);
        } catch (JSONException e) {
            return "";
        }
    }
}
