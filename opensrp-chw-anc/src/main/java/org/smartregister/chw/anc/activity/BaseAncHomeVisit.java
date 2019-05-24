package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.view.activity.SecuredActivity;

import timber.log.Timber;

public class BaseAncHomeVisit extends SecuredActivity implements View.OnClickListener {

    public static void startMe(Activity activity, String memberBaseEntityID) {
        Intent intent = new Intent(activity, BaseAncHomeVisit.class);
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, memberBaseEntityID);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_anc_homevisit);
    }

    @Override
    protected void onCreation() {
        Timber.v("Empty onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("Empty onResumption");
    }

    @Override
    public void onClick(View v) {
        Timber.v("Empty onClick");
    }
}
