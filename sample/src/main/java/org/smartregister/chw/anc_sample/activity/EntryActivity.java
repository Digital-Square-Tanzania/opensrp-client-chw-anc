package org.smartregister.chw.anc_sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.joda.time.LocalDate;
import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.domain.VaccineDisplay;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.chw.anc.fragment.BaseHomeVisitImmunizationFragment;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.VaccineScheduleUtil;
import org.smartregister.chw.anc_sample.R;
import org.smartregister.chw.anc_sample.utils.Constants;
import org.smartregister.chw.pnc.activity.BasePncHomeVisitActivity;
import org.smartregister.chw.pnc.activity.BasePncMemberProfileActivity;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.view.activity.SecuredActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class EntryActivity extends SecuredActivity implements View.OnClickListener, BaseAncHomeVisitContract.VisitView {

    private BaseHomeVisitImmunizationFragment immunizationFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.anc_activity).setOnClickListener(this);
        findViewById(R.id.anc_home_visit).setOnClickListener(this);
        findViewById(R.id.anc_profile).setOnClickListener(this);
        findViewById(R.id.pnc_activity).setOnClickListener(this);
        findViewById(R.id.pnc_home_visit).setOnClickListener(this);
        findViewById(R.id.pnc_profile).setOnClickListener(this);
        findViewById(R.id.immunization_fragment).setOnClickListener(this);
        findViewById(R.id.home_visit_multi_option).setOnClickListener(this);
        findViewById(R.id.home_visit_fragment).setOnClickListener(this);
    }

    @Override
    protected void onCreation() {
        Timber.v("onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("onCreation");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anc_activity:
                startActivity(new Intent(this, AncRegisterActivity.class));
                break;
            case R.id.anc_home_visit:
                AncHomeVisitActivity.startMe(this, "12345", false);
                break;
            case R.id.anc_profile:
                AncMemberProfileActivity.startMe(this, "12345");
                break;
            case R.id.pnc_activity:
                startActivity(new Intent(this, PncRegisterActivity.class));
                break;
            case R.id.pnc_home_visit:
                PncHomeVisitActivity.startMe(this, "12345", false);
                break;
            case R.id.pnc_profile:
                BasePncMemberProfileActivity.startMe(this, "12345");
                //BasePncMemberProfileActivity.startMe(this, EntryActivity.getSampleMember(), "Juma Family Head", "0976345634");
                break;
            case R.id.immunization_fragment:
                openImmunizationFrag();
                break;
            case R.id.home_visit_multi_option:
                BaseAncHomeVisitFragment.getInstance(this, "muac", null, null, null).show(getFragmentManager(), "HV");
                break;
            case R.id.home_visit_fragment:
                BaseAncHomeVisitFragment.getInstance(this, Constants.HOME_VISIT_FORMS.IMMUNIZATION, null, null, null).show(getFragmentManager(), "HV");
                break;
            default:
                break;
        }
    }

    private void openImmunizationFrag() {
        if (immunizationFragment == null) {
            immunizationFragment = BaseHomeVisitImmunizationFragment.getInstance(this, "123345", null, getFakeVaccines());
        }
        immunizationFragment.show(getFragmentManager(), "HV");
    }

    public static MemberObject getSampleMember() {
        Map<String, String> details = new HashMap<>();
        details.put(DBConstants.KEY.FIRST_NAME, "Glory");
        details.put(DBConstants.KEY.LAST_NAME, "Juma");
        details.put(DBConstants.KEY.MIDDLE_NAME, "Wambui");
        details.put(DBConstants.KEY.DOB, "1982-01-18T03:00:00.000+03:00");
        details.put(DBConstants.KEY.LAST_HOME_VISIT, "");
        details.put(DBConstants.KEY.VILLAGE_TOWN, "Lavingtone #221");
        details.put(DBConstants.KEY.FAMILY_NAME, "Jumwa");
        details.put(DBConstants.KEY.UNIQUE_ID, "3503504");
        details.put(DBConstants.KEY.LAST_MENSTRUAL_PERIOD, "04-01-2019");
        details.put(DBConstants.KEY.BASE_ENTITY_ID, "3503504");
        details.put(DBConstants.KEY.FAMILY_HEAD, "3503504");
        details.put(DBConstants.KEY.PRIMARY_CAREGIVER, "3503504");
        details.put(DBConstants.KEY.PHONE_NUMBER, "0934567543");
        CommonPersonObjectClient commonPersonObject = new CommonPersonObjectClient("", details, "Yo");
        commonPersonObject.setColumnmaps(details);

        return new MemberObject(commonPersonObject);
    }

    private List<VaccineDisplay> getFakeVaccines() {
        List<VaccineDisplay> vaccineDisplays = new ArrayList<>();

        Map<String, VaccineGroup> groupMap = VaccineScheduleUtil.getVaccineGroups(this, "child");
        Iterator<VaccineGroup> iterator = groupMap.values().iterator();

        // get first group
        for (Vaccine vaccine : iterator.next().vaccines) {
            VaccineDisplay display = new VaccineDisplay();
            display.setVaccineWrapper(getVaccineWrapper(VaccineRepo.getVaccine(vaccine.name, "child")));
            display.setStartDate(new LocalDate().plusDays(-300).toDate());
            display.setEndDate(new Date());
            vaccineDisplays.add(display);
        }
        return vaccineDisplays;
    }

    private VaccineWrapper getVaccineWrapper(VaccineRepo.Vaccine vaccine) {
        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setVaccine(vaccine);
        vaccineWrapper.setName(vaccine.display());
        vaccineWrapper.setDefaultName(vaccine.display());
        return vaccineWrapper;
    }

    @Override
    public void onDialogOptionUpdated(String jsonString) {
        Timber.v("onDialogOptionUpdated %s", jsonString);
    }

    @Override
    public Context getMyContext() {
        return this;
    }
}
