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
import android.widget.RadioButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;
import org.smartregister.chw.anc.model.BaseAncHomeVisitFragmentModel;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitFragmentPresenter;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.view.customcontrols.CustomFontTextView;

import timber.log.Timber;

public class BaseAncHomeVisitFragment extends DialogFragment implements View.OnClickListener, BaseAncHomeVisitFragmentContract.View {

    private BaseAncHomeVisitContract.View homeVisitView;
    private String title;
    private String question;
    private QuestionType questionType;
    @DrawableRes
    private int imageRes;
    private JSONObject jsonObject;

    private BaseAncHomeVisitFragmentContract.Presenter presenter;

    private CustomFontTextView customFontTextViewTitle;
    private CustomFontTextView customFontTextViewQuestion;
    private ImageView imageViewMain;
    private RadioButton radioButtonYes;
    private RadioButton radioButtonNo;

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

        customFontTextViewTitle = view.findViewById(R.id.customFontTextViewTitle);
        customFontTextViewTitle.setText(getTitle());

        customFontTextViewQuestion = view.findViewById(R.id.customFontTextViewQuestion);
        customFontTextViewQuestion.setText(getQuestion());

        imageViewMain = view.findViewById(R.id.imageViewMain);
        imageViewMain.setImageResource(getImageRes());

        customizeQuestionType();

        radioButtonYes = view.findViewById(R.id.radioButtonYes);
        radioButtonYes.setOnClickListener(this);
        radioButtonNo = view.findViewById(R.id.radioButtonNo);
        radioButtonNo.setOnClickListener(this);

        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.buttonSave).setOnClickListener(this);

        initializePresenter();

        return view;
    }


    private void customizeQuestionType() {
        if (getQuestionType() == null) {
            return;
        }
        switch (getQuestionType()) {
            case BOOLEAN:
                prepareBooleanView();
                break;
            case DATE_SELECTOR:
                prepareDateView();
                break;
            case MULTI_OPTIONS:
                prepareOptionView();
                break;
            default:
                prepareBooleanView();
                break;
        }
    }

    private void prepareBooleanView() {
        Timber.v("prepareBooleanView");
    }

    private void prepareDateView() {
        Timber.v("prepareDateView");
    }

    private void prepareOptionView() {
        Timber.v("prepareOptionView");
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
        Timber.v("showProgressBar");
    }

    @Override
    public Context getMyContext() {
        return getActivity().getApplicationContext();
    }

    public void setTitle(String title) {
        this.title = title;
        if (customFontTextViewTitle != null) {
            customFontTextViewTitle.setText(this.title);
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
        if (customFontTextViewQuestion != null) {
            customFontTextViewQuestion.setText(this.question);
        }
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
        if (imageViewMain != null) {
            imageViewMain.setImageResource(this.imageRes);
        }
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
        customizeQuestionType();
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseAncHomeVisitFragmentPresenter(this, new BaseAncHomeVisitFragmentModel());
    }

    @Override
    public BaseAncHomeVisitFragmentContract.Presenter getPresenter() {
        return presenter;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public void setValue(String value) {
        if (radioButtonNo != null && radioButtonYes != null) {
            if (value.equalsIgnoreCase("Yes")) {
                radioButtonYes.setSelected(true);
                radioButtonNo.setSelected(false);
            } else {
                radioButtonYes.setSelected(false);
                radioButtonNo.setSelected(true);
            }
        }
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setFormName(String formName) {
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
        getHomeVisitView().onDialogOptionUpdated(getJsonObject().toString());
        dismiss();
    }

    protected void selectOption(String option) {
        getPresenter().writeValue(getJsonObject(), option);
    }

    public enum QuestionType {
        BOOLEAN, DATE_SELECTOR, MULTI_OPTIONS
    }
}
