package org.smartregister.chw.anc.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.model.BaseAncHomeVisitFragmentModel;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitFragmentPresenter;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.util.DatePickerUtils;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.util.JsonFormUtils.fields;

public class BaseAncHomeVisitFragment extends BaseHomeVisitFragment implements View.OnClickListener, BaseAncHomeVisitFragmentContract.View {

    private BaseAncHomeVisitContract.VisitView homeVisitView;
    private String title;
    private String question;
    private QuestionType questionType;
    private String infoIconTitle;
    private String infoIconDetails;
    @DrawableRes
    private int imageRes;
    private String count = "1";

    private BaseAncHomeVisitFragmentContract.Presenter presenter;

    private CustomFontTextView customFontTextViewTitle;
    private CustomFontTextView customFontTextViewQuestion;
    private ImageView imageViewMain;
    private RadioGroup radioGroupChoices;
    private RadioButton radioButtonYes;
    private RadioButton radioButtonNo;
    private Button buttonCancel;
    private Button buttonSave;
    private ImageView infoIcon;
    private DatePicker datePicker;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static BaseAncHomeVisitFragment getInstance(final BaseAncHomeVisitContract.VisitView view, String form_name, JSONObject json, Map<String, List<VisitDetail>> details, String count) {
        JSONObject jsonObject = json;
        if (StringUtils.isNotBlank(form_name) && json == null) {
            try {
                jsonObject = JsonFormUtils.getFormAsJson(form_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (details != null && details.size() > 0) {
            JsonFormUtils.populateForm(jsonObject, details);
        }

        BaseAncHomeVisitFragment fragment = new BaseAncHomeVisitFragment();
        fragment.setHomeVisitView(view);
        fragment.setJsonObject(jsonObject);
        fragment.setFormName(form_name);
        fragment.setCount(count);
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

        radioGroupChoices = view.findViewById(R.id.radioGroupChoices);
        radioButtonYes = view.findViewById(R.id.radioButtonYes);
        radioButtonYes.setOnClickListener(this);
        radioButtonNo = view.findViewById(R.id.radioButtonNo);
        radioButtonNo.setOnClickListener(this);

        datePicker = view.findViewById(R.id.datePicker);
        DatePickerUtils.themeDatePicker(datePicker, new char[]{'d', 'm', 'y'});
        datePicker.setMaxDate((new Date()).getTime());

        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);

        infoIcon = view.findViewById(R.id.info_icon);
        infoIcon.setOnClickListener(this);

        buttonSave = view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);

        initializePresenter();
        customizeQuestionType();

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
        // hide date picker and cancel option. Enable radio group and save
        radioGroupChoices.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.VISIBLE);

        datePicker.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.GONE);
    }

    private void prepareDateView() {
        // hide date picker and cancel option. Enable radio group and save
        radioGroupChoices.setVisibility(View.GONE);

        buttonSave.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.VISIBLE);
    }

    private void prepareOptionView() {
        Timber.v("prepareOptionView");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (customFontTextViewTitle != null) {
            customFontTextViewTitle.setText(this.title);
        }
    }

    @Override
    public void showProgressBar(boolean status) {
        Timber.v("showProgressBar");
    }

    @Override
    public Context getMyContext() {
        return getActivity();
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

    public BaseAncHomeVisitContract.VisitView getHomeVisitView() {
        return homeVisitView;
    }

    public void setHomeVisitView(BaseAncHomeVisitContract.VisitView homeVisitView) {
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

    public String getInfoIconTitle() {
        return infoIconTitle;
    }

    @Override
    public void setInfoIconTitle(String infoIconTitle) {
        this.infoIconTitle = infoIconTitle;
        initializeInfoIcon();
    }

    public String getInfoIconDetails() {
        return infoIconDetails;
    }

    @Override
    public void setInfoIconDetails(String infoIconDetails) {
        this.infoIconDetails = infoIconDetails;
        initializeInfoIcon();
    }

    private void initializeInfoIcon() {
        if (StringUtils.isNotBlank(infoIconTitle) && StringUtils.isNotBlank(infoIconDetails)) {
            infoIcon.setVisibility(View.VISIBLE);
        } else {
            infoIcon.setVisibility(View.GONE);
        }
    }

    protected void onShowInfo() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getView().getContext(), com.vijay.jsonwizard.R.style.AppThemeAlertDialog);
        builderSingle.setTitle(getInfoIconTitle());
        builderSingle.setMessage(getInfoIconDetails());
        builderSingle.setIcon(com.vijay.jsonwizard.R.drawable.dialog_info_filled);

        builderSingle.setNegativeButton(getView().getContext().getResources().getString(com.vijay.jsonwizard.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.show();
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

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public void setValue(String value) {
        if (getQuestionType() == QuestionType.BOOLEAN) {
            if (radioButtonNo != null && radioButtonYes != null) {
                if (value.equalsIgnoreCase("Yes")) {
                    radioButtonYes.setChecked(true);
                    radioButtonNo.setChecked(false);
                } else if (value.equalsIgnoreCase("No")) {
                    radioButtonYes.setChecked(false);
                    radioButtonNo.setChecked(true);
                }
            }
        } else if (getQuestionType() == QuestionType.DATE_SELECTOR) {
            // datePicker.updateDate();
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(value));
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            } catch (Exception e) {
                Calendar cal = Calendar.getInstance();
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                Timber.e(e);
            }
        }
    }

    public void setFormName(String formName) {
        if (this.getJsonObject() == null) {
            // load form from assets directory
            try {
                JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);
                // evaluate the count
                if (StringUtils.isNotBlank(count)) {
                    try {
                        // update title
                        String title = jsonObject.getJSONObject("step1").getString("title");
                        jsonObject.getJSONObject("step1").put("title", MessageFormat.format(title, count));

                        // update key
                        JSONArray fields = fields(jsonObject);
                        String key = fields.getJSONObject(0).getString("key");
                        fields.getJSONObject(0).put("key", MessageFormat.format(key, count));

                        String hint = fields.getJSONObject(0).getString("hint");
                        fields.getJSONObject(0).put("hint", MessageFormat.format(hint, count));

                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
                setJsonObject(jsonObject);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.radioButtonYes) {
            onSelectOption("Yes");
        } else if (v.getId() == R.id.radioButtonNo) {
            onSelectOption("No");
        } else if (v.getId() == R.id.buttonSave) {
            onSave();
        } else if (v.getId() == R.id.close) {
            dismiss();
        } else if (v.getId() == R.id.buttonCancel) {
            onCancel();
        } else if (v.getId() == R.id.info_icon) {
            onShowInfo();
        }
    }

    // to support the date values
    protected void onSave() {
        if (getQuestionType() == QuestionType.DATE_SELECTOR) {
            onSelectOption(dateFormat.format(getDateFromDatePicker(datePicker)));
        }

        getHomeVisitView().onDialogOptionUpdated(getJsonObject().toString());
        dismiss();
    }

    protected void onSelectOption(String option) {
        getPresenter().writeValue(getJsonObject(), option);
    }

    protected void onCancel() {
        onSelectOption("Dose not given");
        getHomeVisitView().onDialogOptionUpdated(getJsonObject().toString());
        dismiss();
    }

    public java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public enum QuestionType {
        BOOLEAN, DATE_SELECTOR, MULTI_OPTIONS
    }
}
