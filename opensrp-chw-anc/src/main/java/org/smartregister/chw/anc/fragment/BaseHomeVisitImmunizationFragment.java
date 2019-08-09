package org.smartregister.chw.anc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vijay.jsonwizard.customviews.CheckBox;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.contract.BaseHomeVisitImmunizationFragmentContract;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.model.BaseHomeVisitImmunizationFragmentModel;
import org.smartregister.chw.anc.presenter.BaseHomeVisitImmunizationFragmentPresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class BaseHomeVisitImmunizationFragment extends BaseHomeVisitFragment implements View.OnClickListener, BaseHomeVisitImmunizationFragmentContract.View {

    protected BaseAncHomeVisitContract.VisitView visitView;
    protected String baseEntityID;
    protected Map<String, List<VisitDetail>> details;
    protected BaseHomeVisitImmunizationFragmentContract.Presenter presenter;
    private List<VaccineView> vaccineViews = new ArrayList<>();
    private Map<String, VaccineWrapper> vaccineWrappers = new LinkedHashMap<>();
    private LayoutInflater inflater;
    private LinearLayout multipleVaccineDatePickerView, singleVaccineAddView, vaccinationNameLayout;
    private TextView textViewAddDate;
    private CheckBox checkBoxNoVaccinesDone;
    private DatePicker singleDatePicker;
    private Button saveButton;
    // global minimum date dob should be set if not provided
    private Date minVaccineDate = LocalDate.now().minusYears(5).toDate();

    // global maximum date
    private Date maxVaccineDate = ServiceSchedule.standardiseDateTime(DateTime.now()).toDate();

    public static BaseHomeVisitImmunizationFragment getInstance(final BaseAncHomeVisitContract.VisitView view, String baseEntityID, Date minVaccineDate, Map<String, List<VisitDetail>> details, List<VaccineWrapper> vaccineWrappers) {
        BaseHomeVisitImmunizationFragment fragment = new BaseHomeVisitImmunizationFragment();
        fragment.visitView = view;
        fragment.baseEntityID = baseEntityID;
        fragment.minVaccineDate = minVaccineDate;
        fragment.details = details;
        for (VaccineWrapper vaccineWrapper : vaccineWrappers) {
            fragment.vaccineWrappers.put(vaccineWrapper.getName(), vaccineWrapper);
        }
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_visit_immunization, container, false);
        this.inflater = inflater;

        multipleVaccineDatePickerView = view.findViewById(R.id.multiple_vaccine_date_pickerview);
        singleVaccineAddView = view.findViewById(R.id.single_vaccine_add_layout);
        vaccinationNameLayout = view.findViewById(R.id.vaccination_name_layout);

        saveButton = view.findViewById(R.id.save_btn);
        saveButton.setOnClickListener(this);

        view.findViewById(R.id.close).setOnClickListener(this);

        textViewAddDate = view.findViewById(R.id.add_date_separately);
        textViewAddDate.setOnClickListener(this);

        singleDatePicker = view.findViewById(R.id.earlier_date_picker);
        initializeDatePicker(singleDatePicker);

        checkBoxNoVaccinesDone = view.findViewById(R.id.select);
        checkBoxNoVaccinesDone.setOnClickListener(this);
        checkBoxNoVaccinesDone.setChecked(false);

        addVaccineViews();

        checkBoxNoVaccinesDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setSingleEntryMode(true);
                    for (VaccineView vaccineView : vaccineViews) {
                        if (vaccineView.getCheckBox().isChecked())
                            vaccineView.getCheckBox().setChecked(false);
                    }
                }

                redrawView();
            }
        });

        initializePresenter();

        return view;
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseHomeVisitImmunizationFragmentPresenter(this, new BaseHomeVisitImmunizationFragmentModel());
    }

    private void addVaccineViews() {
        // get the views and bind the click listener
        vaccineViews.clear();
        for (VaccineWrapper vaccineWrapper : vaccineWrappers.values()) {
            View vaccinationName = inflater.inflate(R.layout.custom_vaccine_name_check, null);
            TextView vaccineView = vaccinationName.findViewById(R.id.vaccine);
            CheckBox checkBox = vaccinationName.findViewById(R.id.select);
            VaccineRepo.Vaccine vaccine = vaccineWrapper.getVaccine();
            final VaccineView view = new VaccineView(vaccineWrapper.getName(), null, checkBox);
            vaccineView.setText((vaccineWrapper.getVaccine() != null) ? vaccine.display() : vaccineWrapper.getName());

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkBoxNoVaccinesDone.setChecked(false);
                    } else {
                        // check if there are any active vaccine
                        boolean enableNoVaccines = true;
                        for (VaccineView vaccineView : vaccineViews) {
                            if (vaccineView.getCheckBox().isChecked())
                                enableNoVaccines = false;
                        }

                        if (enableNoVaccines && !checkBoxNoVaccinesDone.isChecked())
                            checkBoxNoVaccinesDone.setChecked(true);
                    }
                    redrawView();
                }
            });
            vaccinationNameLayout.addView(vaccinationName);
            vaccineViews.add(view);
        }
    }

    private void initializeDatePicker(DatePicker datePicker) {
        if (minVaccineDate.getTime() > maxVaccineDate.getTime()) {
            datePicker.setMinDate(minVaccineDate.getTime());
            datePicker.setMaxDate(minVaccineDate.getTime());
        } else {
            datePicker.setMinDate(minVaccineDate.getTime());
            datePicker.setMaxDate(maxVaccineDate.getTime());
        }
    }

    private Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private void setDateFromDatePicker(DatePicker datePicker, Date date) {
        datePicker.init(date.getYear(), date.getMonth(), date.getDay(), null);
    }

    /**
     * activated when each vaccine has a different date
     */
    private void onVariedResponsesMode() {
        // global state
        setSingleEntryMode(false);

        // create a number of date piker views with the injected heading
        singleVaccineAddView.removeAllViews();
        generateDatePickerViews();
        redrawView();
    }

    private void generateDatePickerViews() {
        int x = 0;
        while (vaccineViews.size() > x) {
            VaccineView vaccineView = vaccineViews.get(x);

            View layout = inflater.inflate(R.layout.custom_single_vaccine_view, null);
            TextView question = layout.findViewById(R.id.vaccines_given_when_title_question);
            DatePicker datePicker = layout.findViewById(R.id.earlier_date_picker);
            question.setText(getString(R.string.when_vaccine, vaccineView.vaccineName));
            initializeDatePicker(datePicker);
            vaccineView.setDatePickerView(datePicker);

            singleVaccineAddView.addView(layout);
            x++;
        }
    }

    /**
     * notifies the host view of all the selected values
     * by sending a json object with the details
     */
    private void onSave() {
        // notify the view (write to json file then dismiss)

        Date vaccineDate = getDateFromDatePicker(singleDatePicker);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMATS.NATIVE_FORMS, Locale.getDefault());
        HashMap<VaccineWrapper, String> vaccineDateMap = new HashMap<>();

        boolean multiModeActive = multipleVaccineDatePickerView.getVisibility() == View.GONE;

        for (VaccineView vaccineView : vaccineViews) {
            VaccineWrapper wrapper = vaccineWrappers.get(vaccineView.getVaccineName());
            if (wrapper != null) {
                if (!checkBoxNoVaccinesDone.isChecked() && vaccineView.getCheckBox().isChecked()) {
                    if (vaccineView.getDatePickerView() != null && multiModeActive) {
                        vaccineDateMap.put(wrapper, dateFormat.format(getDateFromDatePicker(vaccineView.getDatePickerView())));
                    } else if (vaccineDate != null) {
                        vaccineDateMap.put(wrapper, dateFormat.format(vaccineDate));
                    }
                } else {
                    vaccineDateMap.put(wrapper, Constants.HOME_VISIT.VACCINE_NOT_GIVEN);
                }
            }
        }

        // create a json object and write values to it that have the vaccine dates
        jsonObject = NCUtils.getVisitJSONFromWrapper(baseEntityID, vaccineDateMap);

        // notify the view
        if (jsonObject != null) {
            visitView.onDialogOptionUpdated(jsonObject.toString());

            // save the view
            dismiss();
        }
    }

    /**
     * executed to close the vaccine screen
     */
    private void onClose() {
        dismiss();
    }

    /**
     * Is called on every ui updating action
     */
    public void redrawView() {
        boolean noVaccine = true;
        for (VaccineView vaccineView : vaccineViews) {
            // enable or disable views
            if (vaccineView.getDatePickerView() != null) {
                ((View) vaccineView.getDatePickerView().getParent()).setVisibility(vaccineView.getCheckBox().isChecked() ? View.VISIBLE : View.GONE);
            }

            if (vaccineView.getCheckBox().isChecked())
                noVaccine = false;
        }

        if (noVaccine) {
            multipleVaccineDatePickerView.setAlpha(0.3f);
        } else {
            multipleVaccineDatePickerView.setAlpha(1.0f);
            saveButton.setAlpha(1.0f);
        }
    }

    /**
     * toggle vaccine entry mode
     *
     * @param state
     */
    private void setSingleEntryMode(boolean state) {
        textViewAddDate.setVisibility(state ? View.VISIBLE : View.GONE);
        multipleVaccineDatePickerView.setVisibility(state ? View.VISIBLE : View.GONE);
        singleVaccineAddView.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        if (viewID == R.id.save_btn) {
            onSave();
        } else if (viewID == R.id.add_date_separately) {
            onVariedResponsesMode();
        } else if (viewID == R.id.close) {
            onClose();
        }
    }

    @Override
    public void updateNoVaccineCheck(boolean state) {
        checkBoxNoVaccinesDone.setChecked(state);
    }

    @Override
    public void updateSelectedVaccines(Map<String, String> selectedVaccines, boolean variedMode) {
        if (variedMode)
            generateDatePickerViews();

        Map<String, VaccineView> lookup = new HashMap<>();
        for (VaccineView vaccineView : vaccineViews) {
            lookup.put(NCUtils.removeSpaces(vaccineView.vaccineName), vaccineView);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMATS.NATIVE_FORMS, Locale.getDefault());
        for (Map.Entry<String, String> entry : selectedVaccines.entrySet()) {
            VaccineView vaccineView = lookup.get(entry.getKey());
            if (vaccineView != null) {
                if (entry.getValue().equalsIgnoreCase(Constants.HOME_VISIT.VACCINE_NOT_GIVEN)) {
                    vaccineView.getCheckBox().setChecked(false);
                } else {
                    vaccineView.getCheckBox().setChecked(true);
                    try {
                        DatePicker datePicker = vaccineView.getDatePickerView();
                        if (datePicker == null)
                            datePicker = singleDatePicker;

                        setDateFromDatePicker(datePicker, sdf.parse(entry.getValue()));
                    } catch (ParseException e) {
                        Timber.e(e);
                    }
                }
            }
        }

        setSingleEntryMode(!variedMode);
    }

    /**
     * holding container
     */
    private class VaccineView {
        private String vaccineName;
        private DatePicker datePickerView;
        private CheckBox checkBox;

        public VaccineView(String vaccineName, DatePicker datePickerView, CheckBox checkBox) {
            this.vaccineName = vaccineName;
            this.datePickerView = datePickerView;
            this.checkBox = checkBox;
        }

        public String getVaccineName() {
            return vaccineName;
        }

        public DatePicker getDatePickerView() {
            return datePickerView;
        }

        public void setDatePickerView(DatePicker datePickerView) {
            this.datePickerView = datePickerView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }
}
