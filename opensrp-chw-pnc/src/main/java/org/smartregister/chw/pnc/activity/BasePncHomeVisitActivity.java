package org.smartregister.chw.pnc.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.activity.BaseAncHomeVisitActivity;
import org.smartregister.chw.anc.domain.MemberObject;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.EDIT_MODE;
import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;

public class BasePncHomeVisitActivity extends BaseAncHomeVisitActivity {

    public static void startMe(Activity activity, MemberObject memberObject, Boolean isEditMode) {
        Intent intent = new Intent(activity, BasePncHomeVisitActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        intent.putExtra(EDIT_MODE, isEditMode);
        activity.startActivity(intent);
    }
}
