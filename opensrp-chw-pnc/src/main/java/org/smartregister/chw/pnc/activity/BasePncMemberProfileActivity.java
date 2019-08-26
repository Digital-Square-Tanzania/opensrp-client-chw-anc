package org.smartregister.chw.pnc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.interactor.BasePncMemberProfileInteractor;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.text.MessageFormat;
import java.util.Date;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.FAMILY_HEAD_NAME;
import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.FAMILY_HEAD_PHONE;
import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;
import static org.smartregister.util.Utils.getName;

public class BasePncMemberProfileActivity extends BaseAncMemberProfileActivity {
    private BasePncMemberProfileInteractor basePncMemberProfileInteractor = new BasePncMemberProfileInteractor();

    public static void startMe(Activity activity, MemberObject memberObject, String familyHeadName, String familyHeadPhoneNumber) {
        Intent intent = new Intent(activity, BasePncMemberProfileActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        intent.putExtra(FAMILY_HEAD_NAME, familyHeadName);
        intent.putExtra(FAMILY_HEAD_PHONE, familyHeadPhoneNumber);
        activity.startActivity(intent);
    }


    @Override
    protected void setupViews() {
        super.setupViews();
        CustomFontTextView titleView = findViewById(R.id.toolbar_title);
        titleView.setText(getString(R.string.return_to_all_pnc_women));
        record_reccuringvisit_done_bar.setVisibility(View.GONE);
        textViewAncVisitNot.setVisibility(View.GONE);
        imageView = findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.imageview_profile);

    }


    @Override
    public void setMemberName(String memberName) {
        basePncMemberProfileInteractor.getPncMotherNameDetails(MEMBER_OBJECT, text_view_anc_member_name, imageView);
    }

    @Override
    public void setMemberGA(String memberGA) {
        String pncDay = basePncMemberProfileInteractor.getPncDay(MEMBER_OBJECT.getBaseEntityId());
        if (pncDay != null) {
            text_view_ga.setText(getName(getString(R.string.pnc_day), pncDay));
        }
    }

    @Override
    protected String getProfileType() {
        return Constants.MEMBER_PROFILE_TYPES.PNC;
    }

    @Override
    public void setRecordVisitTitle(String title) {
        textview_record_anc_visit.setText(getString(R.string.record_pnc_visit));
    }


    @Override
    public void openMedicalHistory() {
        BasePncMedicalHistoryActivity.startMe(this, MEMBER_OBJECT);
    }


    @Override
    public void setLastVisit(Date lastVisitDate) {
        view_last_visit_row.setVisibility(View.VISIBLE);
        if (basePncMemberProfileInteractor.getLastVisitDate(MEMBER_OBJECT.getBaseEntityId()) != null) {
            rlLastVisit.setVisibility(View.VISIBLE);
            String x = basePncMemberProfileInteractor.getLastVisitDate(MEMBER_OBJECT.getBaseEntityId());
            tvLastVisitDate.setText(MessageFormat.format(getString(R.string.pnc_last_visit_text), x));
        }


    }

}
