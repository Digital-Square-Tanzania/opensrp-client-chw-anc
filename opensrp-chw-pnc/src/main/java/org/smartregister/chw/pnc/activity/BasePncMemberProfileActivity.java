package org.smartregister.chw.pnc.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.interactor.BasePncMemberProfileInteractor;
import org.smartregister.view.customcontrols.CustomFontTextView;

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
        record_reccuringvisit_done_bar.setVisibility(view_anc_record.GONE);
        textViewAncVisitNot.setVisibility(view_anc_record.GONE);
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
}
