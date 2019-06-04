package org.smartregister.chw.anc_sample.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.activity.BaseAncHomeVisitActivity;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitPresenter;
import org.smartregister.chw.anc_sample.interactor.AncHomeVisitInteractor;

public class AncHomeVisitActivity extends BaseAncHomeVisitActivity {

    public static void startMe(Activity activity, String memberBaseEntityID) {
        Intent intent = new Intent(activity, AncHomeVisitActivity.class);
        intent.putExtra("BASE_ENTITY_ID", memberBaseEntityID);
        activity.startActivity(intent);
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseAncHomeVisitPresenter(BASE_ENTITY_ID, this, new AncHomeVisitInteractor());
    }
}
