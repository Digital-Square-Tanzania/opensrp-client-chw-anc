package org.smartregister.chw.anc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vijay.jsonwizard.customviews.CheckBox;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseHomeVisitImmunizationFragment extends BaseHomeVisitFragment implements View.OnClickListener {

    protected BaseAncHomeVisitContract.VisitView visitView;
    protected String baseEntityID;
    protected Map<String, List<VisitDetail>> details;
    private List<VaccineView> vaccineViews = new ArrayList<>();
    private List<VaccineWrapper> vaccineWrappers = new ArrayList<>();
    private boolean individualVaccineMode = false;
    private boolean noVaccineSelected = false;

    public static BaseHomeVisitImmunizationFragment getInstance(final BaseAncHomeVisitContract.VisitView view, String baseEntityID, Map<String, List<VisitDetail>> details) {
        BaseHomeVisitImmunizationFragment fragment = new BaseHomeVisitImmunizationFragment();
        fragment.visitView = view;
        fragment.baseEntityID = baseEntityID;
        fragment.details = details;
        return fragment;
    }

    private LinearLayout multipleVaccineDatePickerView, singleVaccineAddView, vaccinationNameLayout;
    private TextView textViewAddDate;
    private CheckBox checkBoxNoVaccinesDone;

    //TODO delete these vaccines
    private void getFakeVaccines() {
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setName("OPV 2");
        wrapper.setVaccine(VaccineRepo.Vaccine.opv2);
        vaccineWrappers.add(wrapper);

        VaccineWrapper wrapper1 = new VaccineWrapper();
        wrapper1.setName("Penta 1");
        wrapper.setVaccine(VaccineRepo.Vaccine.penta1);
        vaccineWrappers.add(wrapper1);

        VaccineWrapper wrapper2 = new VaccineWrapper();
        wrapper2.setName("PCV 2");
        wrapper.setVaccine(VaccineRepo.Vaccine.pcv2);
        vaccineWrappers.add(wrapper2);

        VaccineWrapper wrapper3 = new VaccineWrapper();
        wrapper3.setName("Rota 2");
        wrapper.setVaccine(VaccineRepo.Vaccine.rota2);
        vaccineWrappers.add(wrapper3);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_visit_immunization, container, false);

        getFakeVaccines();// delete this method

        multipleVaccineDatePickerView = view.findViewById(R.id.multiple_vaccine_date_pickerview);
        singleVaccineAddView = view.findViewById(R.id.single_vaccine_add_layout);
        vaccinationNameLayout = view.findViewById(R.id.vaccination_name_layout);

        view.findViewById(R.id.save_btn).setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);

        textViewAddDate = view.findViewById(R.id.add_date_separately);
        textViewAddDate.setOnClickListener(this);

        checkBoxNoVaccinesDone = view.findViewById(R.id.select);
        checkBoxNoVaccinesDone.setOnClickListener(this);
        checkBoxNoVaccinesDone.setChecked(false);
        checkBoxNoVaccinesDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onNoVaccinesDoneToggle(isChecked);
            }
        });

        addVaccineViews(inflater);

        return view;
    }

    private void addVaccineViews(LayoutInflater inflater) {
        // get the views and bind the click listener
        for (VaccineWrapper vaccineWrapper : vaccineWrappers) {
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
     * is executed each time a vaccine date is adjusted
     */
    private void onViewStateVaccineDone() {
        redrawView();
    }

    /**
     * i
     */
    private void onVariedResponsesMode() {
        textViewAddDate.setVisibility(View.GONE);
        multipleVaccineDatePickerView.setVisibility(View.GONE);
        singleVaccineAddView.setVisibility(View.VISIBLE);
    }

    /**
     * returns a payload of all the selected values
     */
    private void onSave() {
// notify the view

        dismiss();
    }

    /**
     * executed to close the vaccine screen
     */
    private void onClose() {
        dismiss();
    }

    // add the vaccine group children
    private void intializeViews() {
        // add the list of vaccines

    }

    /**
     * Is called on every ui updating action
     */
    public void redrawView() {
        boolean noVaccine = true;
        for (VaccineView vaccineView : vaccineViews) {
            if (vaccineView.getCheckBox().isChecked())
                noVaccine = false;
        }


        checkBoxNoVaccinesDone.setChecked(noVaccine);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        if (viewID == R.id.checkbox_no_vaccination) {

        } else if (viewID == R.id.save_btn) {

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
