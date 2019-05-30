package org.smartregister.chw.anc.contract;

import android.content.Context;

import org.json.JSONObject;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;

public interface BaseAncHomeVisitFragmentContract {

    interface View {

        void initializePresenter();

        Presenter getPresenter();

        void showProgressBar(boolean status);

        Context getMyContext();

        void setBooleanChoiceState(Boolean isYes);

        void setTitle(String title);

        void setQuestion(String question);

        void setImageRes(int imageRes);

        void setQuestionType(BaseAncHomeVisitFragment.QuestionType questionType);

        JSONObject getJsonObject();
    }

    interface Presenter {

        /**
         * Start up routine for the presenter.
         */
        void initialize();

        void setTitle(String title);

        void setQuestion(String question);

        void setImageRes(int imageRes);

        void setQuestionType(BaseAncHomeVisitFragment.QuestionType questionType);

        View getView();

    }

    interface Model {

        /**
         * Receives a jsonObject compatible with native forms and renders the 1st question only
         * The values, choices and type of question displayed based on the native for compatibility
         *
         * @param jsonObject
         * @param presenter
         */
        void processJson(JSONObject jsonObject, Presenter presenter);

        /**
         * Updates the value of the question options with the selected value
         *
         * @param jsonObject
         * @param key
         * @param value
         */
        void writeValue(JSONObject jsonObject, String key, String value);
    }

}
