package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.view.activity.SecuredActivity;

public class BaseAncMemberProfile extends SecuredActivity implements View.OnClickListener {

    public static void startMe(Activity activity, String memberBaseEntityID) {
        Intent intent = new Intent(activity, BaseAncMemberProfile.class);
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, memberBaseEntityID);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_anc_member_profile);
    }

    @Override
    protected void onCreation() {

    }

    @Override
    protected void onResumption() {

    }

    @Override
    public void onClick(View v) {

    }
}
