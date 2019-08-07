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
import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.util.Util;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseHomeVisitImmunizationFragment extends BaseHomeVisitFragment implements View.OnClickListener {

    protected BaseAncHomeVisitContract.VisitView visitView;
    protected String baseEntityID;
    protected Map<String, List<VisitDetail>> details;
    private List<VaccineView> vaccineViews = new ArrayList<>();
    private Map<String, VaccineWrapper> vaccineWrappers = new LinkedHashMap<>();
    private boolean individualVaccineMode = false;

    // global minimum date dob should be set if not provided
    private Date minVaccineDate = LocalDate.now().minusYears(5).toDate();

    // global maximum date
    private Date maxVaccineDate = ServiceSchedule.standardiseDateTime(DateTime.now()).toDate();

    public static BaseHomeVisitImmunizationFragment getInstance(final BaseAncHomeVisitContract.VisitView view, String baseEntityID, Map<String, List<VisitDetail>> details) {
        BaseHomeVisitImmunizationFragment fragment = new BaseHomeVisitImmunizationFragment();
        fragment.visitView = view;
        fragment.baseEntityID = baseEntityID;
        fragment.details = details;
        return fragment;
    }

    private LayoutInflater inflater;
    private LinearLayout multipleVaccineDatePickerView, singleVaccineAddView, vaccinationNameLayout;
    private TextView textViewAddDate;
    private CheckBox checkBoxNoVaccinesDone;
    private DatePicker singleDatePicker;
    private Button saveButton;

    //TODO delete these vaccines
    private void getFakeVaccines() {
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setName("OPV 2");
        wrapper.setVaccine(VaccineRepo.Vaccine.opv2);
        vaccineWrappers.put(wrapper.getName(), wrapper);

        VaccineWrapper wrapper1 = new VaccineWrapper();
        wrapper1.setName("Penta 1");
        wrapper1.setVaccine(VaccineRepo.Vaccine.penta1);
        vaccineWrappers.put(wrapper1.getName(), wrapper1);

        VaccineWrapper wrapper2 = new VaccineWrapper();
        wrapper2.setName("PCV 2");
        wrapper2.setVaccine(VaccineRepo.Vaccine.pcv2);
        vaccineWrappers.put(wrapper2.getName(), wrapper2);

        VaccineWrapper wrapper3 = new VaccineWrapper();
        wrapper3.setName("Rota 2");
        wrapper3.setVaccine(VaccineRepo.Vaccine.rota2);
        vaccineWrappers.put(wrapper3.getName(), wrapper3);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_visit_immunization, container, false);
        this.inflater = inflater;

        getFakeVaccines();// delete this method

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
        checkBoxNoVaccinesDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onNoVaccinesDoneToggle(isChecked);
            }
        });

        addVaccineViews();

        return view;
    }

    private void addVaccineViews() {
        // get the views and bind the click listener
        for (VaccineWrapper vaccineWrapper : vaccineWrappers.values()) {
            View vaccinationName = inflater.inflate(R.layout.custom_vaccine_name_check, null);
            TextView vaccineView = vaccinationName.findViewById(R.id.vaccine);
            CheckBox checkBox = vaccinationName.findViewById(R.id.select);
            VaccineRepo.Vaccine vaccine = vaccineWrapper.getVaccine();
            final VaccineView view = new VaccineView(vaccineWrapper.getName(), null, checkBox);
            if (vaccineWrapper.getVaccine() != null) {
                vaccineView.setText(vaccine.display());
            } else {
                vaccineView.setText(vaccineWrapper.getName());
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onViewStateVaccineDone();
                }
            });
            vaccinationNameLayout.addView(vaccinationName);
            vaccineViews.add(view);
        }
    }

    /**
     * execute when no vaccine done is selected
     */
    private void onNoVaccinesDoneToggle(boolean isActive) {
        if (isActive) {
            for (VaccineView vaccineView : vaccineViews) {
                vaccineView.getCheckBox().setChecked(false);
            }
        }

        redrawView();
    }

    /**
     * is executed each time a vaccine is selected
     */
    private void onViewStateVaccineDone() {
        redrawView();
    }

    /**
     * activated when each vaccine has a different date
     */
    private void onVariedResponsesMode() {
        // global state
        individualVaccineMode = true;
        setSingleEntryMode(false);

        // create a number of date piker views with the injected heading
        singleVaccineAddView.removeAllViews();

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

    private void initializeDatePicker(DatePicker datePicker) {
        if (minVaccineDate.getTime() > maxVaccineDate.getTime()) {
            datePicker.setMinDate(minVaccineDate.getTime());
            datePicker.setMaxDate(minVaccineDate.getTime());
        } else {
            datePicker.setMinDate(minVaccineDate.getTime());
            datePicker.setMaxDate(maxVaccineDate.getTime());
        }
    }

    /**
     * notifies the host view of all the selected values
     * by sending a json object with the details
     */
    private void onSave() {
        // notify the view (write to json file then dismiss)

        Date vaccineDate = getDateFromDatePicker(singleDatePicker);
        Map<VaccineWrapper, Date> vaccineDateMap = new HashMap<>();

        for (VaccineView vaccineView : vaccineViews) {
            if (vaccineView.getCheckBox().isChecked()) {
                VaccineWrapper wrapper = vaccineWrappers.get(vaccineView.getVaccineName());
                if (wrapper != null)
                    vaccineDateMap.put(wrapper, (individualVaccineMode) ? vaccineDate : getDateFromDatePicker(vaccineView.getDatePickerView()));
            }
        }

        // create a json object and write values to it that have the vaccine dates
        JSONObject jsonObject = Util.getVisitJSONFromWrapper(vaccineDateMap);

        // notify the view
        visitView.onDialogOptionUpdated(jsonObject.toString());

        // save the view
        dismiss();
    }

    // reads date from date picker
    private Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
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
            if (individualVaccineMode && vaccineView.getDatePickerView() != null) {
                ((View) vaccineView.getDatePickerView().getParent()).setVisibility(vaccineView.getCheckBox().isChecked() ? View.VISIBLE : View.GONE);
            }

            if (vaccineView.getCheckBox().isChecked())
                noVaccine = false;
        }


        checkBoxNoVaccinesDone.setChecked(noVaccine);
        if (noVaccine) {
            multipleVaccineDatePickerView.setAlpha(0.3f);
            setSingleEntryMode(true);
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
        individualVaccineMode = state;

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

        public void setVaccineName(String vaccineName) {
            this.vaccineName = vaccineName;
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
