package org.smartregister.chw.anc.custom_views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.smartregister.chw.anc.fragment.BaseAncWomanCallDialogFragment;
import org.smartregister.chw.opensrp_chw_anc.R;

public class BaseAncFloatingMenu extends LinearLayout implements View.OnClickListener {
    private String phoneNumber, familyHeadName, familyHeadPhone, womanName;

    public BaseAncFloatingMenu(Context context, String ancWomanName, String ancWomanPhone, String ancFamilyHeadName, String ancFamilyHeadPhone) {
        super(context);
        initUi();
        womanName = ancWomanName;
        phoneNumber = ancWomanPhone;
        familyHeadName = ancFamilyHeadName;
        familyHeadPhone = ancFamilyHeadPhone;
    }

    public BaseAncFloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi();
    }

    public BaseAncFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUi();
    }

    private void initUi() {
        inflate(getContext(), R.layout.view_anc_call_woma_floating_menu, this);

        findViewById(R.id.anc_fab).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                BaseAncWomanCallDialogFragment.launchDialog(activity, womanName, phoneNumber, familyHeadName, familyHeadPhone);
            }
        });
    }

    @Override
    public void onClick(View v) {
//        implement
    }
}