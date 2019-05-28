package org.smartregister.chw.anc.fragment;


import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.opensrp_chw_anc.R;

public class BaseAncHomeVisitFragment extends DialogFragment {

    private BaseAncHomeVisitContract.View homeVisitView;
    private String title;
    private String question;
    private QuestionType questionType;
    @LayoutRes
    private int layoutRes;


    public static BaseAncHomeVisitFragment getInstance(String title, String question, int layoutRes, QuestionType type) {
        BaseAncHomeVisitFragment fragment = new BaseAncHomeVisitFragment();
        fragment.setTitle(title);
        fragment.setQuestion(question);
        fragment.setLayoutRes(layoutRes);
        fragment.setQuestionType(type);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_anc_home_visit, container, false);
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

    public int getLayoutRes() {
        return layoutRes;
    }

    public void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public enum QuestionType {
        BOOLEAN
    }
}
