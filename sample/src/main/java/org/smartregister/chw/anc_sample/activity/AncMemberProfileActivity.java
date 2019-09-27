package org.smartregister.chw.anc_sample.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.presenter.BaseAncMemberProfilePresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc_sample.interactor.AncMemberProfileInteractor;

public class AncMemberProfileActivity extends BaseAncMemberProfileActivity {

    public static void startMe(Activity activity, String baseEntityID) {
        Intent intent = new Intent(activity, AncMemberProfileActivity.class);
        intent.putExtra(Constants.ANC_MEMBER_OBJECTS.BASE_ENTITY_ID, baseEntityID);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_HOME_VISIT);
    }

    protected void registerPresenter() {
        presenter = new BaseAncMemberProfilePresenter(this, new AncMemberProfileInteractor(), memberObject);
    }
}
