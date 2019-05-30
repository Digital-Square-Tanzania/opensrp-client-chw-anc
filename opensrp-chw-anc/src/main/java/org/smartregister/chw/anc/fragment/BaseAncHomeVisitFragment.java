package org.smartregister.chw.anc.fragment;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;
import org.smartregister.chw.anc.model.BaseAncHomeVisitFragmentModel;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitFragmentPresenter;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.opensrp_chw_anc.R;

import timber.log.Timber;

public class BaseAncHomeVisitFragment extends DialogFragment implements View.OnClickListener, BaseAncHomeVisitFragmentContract.View {

    private BaseAncHomeVisitContract.View homeVisitView;
    private String title;
    private String question;
    private QuestionType questionType;
    @DrawableRes
    private int imageRes;
    private String selectedOption;
    private JSONObject jsonObject;
    private String formName;

    private BaseAncHomeVisitFragmentContract.Presenter presenter;

    public static BaseAncHomeVisitFragment getInstance(BaseAncHomeVisitContract.View view, String title, String question, @DrawableRes int imageRes, QuestionType type) {
        BaseAncHomeVisitFragment fragment = new BaseAncHomeVisitFragment();
        fragment.setHomeVisitView(view);
        fragment.setTitle(title);
        fragment.setQuestion(question);
        fragment.setImageRes(imageRes);
        fragment.setQuestionType(type);
        return fragment;
    }


    public static BaseAncHomeVisitFragment getInstance(BaseAncHomeVisitContract.View view, String form_name, JSONObject jsonObject) {
        BaseAncHomeVisitFragment fragment = new BaseAncHomeVisitFragment();
        fragment.setHomeVisitView(view);
        fragment.setJsonObject(jsonObject);
        fragment.setFormName(form_name);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_anc_home_visit, container, false);
        ((TextView) view.findViewById(R.id.customFontTextViewTitle)).setText(getTitle());
        ((TextView) view.findViewById(R.id.customFontTextViewQuestion)).setText(getQuestion());
        ((ImageView) view.findViewById(R.id.imageViewMain)).setImageResource(getImageRes());
        customizeQuestionType();

        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.radioButtonYes).setOnClickListener(this);
        view.findViewById(R.id.radioButtonNo).setOnClickListener(this);
        view.findViewById(R.id.buttonSave).setOnClickListener(this);

        initializePresenter();

        return view;
    }


    private void customizeQuestionType() {
        // hide / show view depending on the question type
        Timber.v("customizeQuestionType");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getDialog() != null && getDialog().getWindow() != null) {
                    getDialog().getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                }
            }
        });

    }

    public String getTitle() {
        return title;
    }

    @Override
    public void showProgressBar(boolean status) {

    }

    @Override
    public Context getMyContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void setBooleanChoiceState(Boolean isYes) {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public BaseAncHomeVisitContract.View getHomeVisitView() {
        return homeVisitView;
    }

    public void setHomeVisitView(BaseAncHomeVisitContract.View homeVisitView) {
        this.homeVisitView = homeVisitView;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseAncHomeVisitFragmentPresenter(this, new BaseAncHomeVisitFragmentModel());
    }

    @Override
    public BaseAncHomeVisitFragmentContract.Presenter getPresenter() {
        return null;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
        if (this.getJsonObject() == null) {
            // load form from assets directory
            try {
                JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);
                setJsonObject(jsonObject);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.radioButtonYes) {
            selectOption("Yes");
        } else if (v.getId() == R.id.radioButtonNo) {
            selectOption("No");
        } else if (v.getId() == R.id.buttonSave) {
            save();
        } else if (v.getId() == R.id.close) {
            dismiss();
        }
    }

    protected void save() {
        homeVisitView.onDialogOptionUpdated(selectedOption);
        dismiss();
    }

    protected void selectOption(String option) {
        selectedOption = option;
    }

    public enum QuestionType {
        BOOLEAN, DATE_SELECTOR, MULTI_OPTIONS
    }
}
