package org.smartregister.chw.anc.fragment;


import android.app.DialogFragment;
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
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.opensrp_chw_anc.R;

public class BaseAncHomeVisitFragment extends DialogFragment implements View.OnClickListener {

    private BaseAncHomeVisitContract.View homeVisitView;
    private String title;
    private String question;
    private QuestionType questionType;
    @DrawableRes
    private int imageRes;


    public static BaseAncHomeVisitFragment getInstance(String title, String question, @DrawableRes int imageRes, QuestionType type) {
        BaseAncHomeVisitFragment fragment = new BaseAncHomeVisitFragment();
        fragment.setTitle(title);
        fragment.setQuestion(question);
        fragment.setImageRes(imageRes);
        fragment.setQuestionType(type);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_anc_home_visit, container, false);
        ((TextView) view.findViewById(R.id.customFontTextViewTitle)).setText(title);
        ((TextView) view.findViewById(R.id.customFontTextViewQuestion)).setText(question);
        ((ImageView) view.findViewById(R.id.imageViewMain)).setImageResource(imageRes);
        customizeQuestionType();
        return view;
    }

    private void customizeQuestionType(){

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
    public void onClick(View v) {

    }

    public enum QuestionType {
        BOOLEAN
    }
}
