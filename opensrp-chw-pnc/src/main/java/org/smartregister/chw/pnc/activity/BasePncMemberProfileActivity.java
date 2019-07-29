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
    protected void onCreation() {
        super.onCreation();
        CustomFontTextView titleView = findViewById(R.id.toolbar_title);
        titleView.setText(getString(R.string.return_to_all_pnc_women));
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
}
