package org.smartregister.chw.anc_sample.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.activity.BaseAncHomeVisitActivity;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitPresenter;
import org.smartregister.chw.anc_sample.interactor.AncHomeVisitInteractor;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.EDIT_MODE;
import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;

public class AncHomeVisitActivity extends BaseAncHomeVisitActivity {

    public static void startMe(Activity activity, MemberObject memberObject, Boolean isEditMode) {
        Intent intent = new Intent(activity, AncHomeVisitActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        intent.putExtra(EDIT_MODE, isEditMode);
        activity.startActivity(intent);
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseAncHomeVisitPresenter(memberObject, this, new AncHomeVisitInteractor());
    }
}
