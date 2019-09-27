package org.smartregister.chw.anc_sample.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.presenter.BaseAncHomeVisitPresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc_sample.interactor.PncHomeVisitInteractor;
import org.smartregister.chw.pnc.activity.BasePncHomeVisitActivity;

public class PncHomeVisitActivity extends BasePncHomeVisitActivity {

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, PncHomeVisitActivity.class);
        intent.putExtra(Constants.ANC_MEMBER_OBJECTS.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ANC_MEMBER_OBJECTS.EDIT_MODE, isEditMode);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_HOME_VISIT);
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseAncHomeVisitPresenter(memberObject, this, new PncHomeVisitInteractor());
    }
}
