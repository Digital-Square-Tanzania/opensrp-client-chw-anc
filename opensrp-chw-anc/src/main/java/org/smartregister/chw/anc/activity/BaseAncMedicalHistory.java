package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.opensrp_chw_anc.R;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;

public class BaseAncMedicalHistory extends AppCompatActivity {

    protected MemberObject memberObject;

    public static void startMe(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, BaseAncMedicalHistory.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_anc_medical_history);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            memberObject = (MemberObject) getIntent().getSerializableExtra(MEMBER_PROFILE_OBJECT);
        }
    }
}
