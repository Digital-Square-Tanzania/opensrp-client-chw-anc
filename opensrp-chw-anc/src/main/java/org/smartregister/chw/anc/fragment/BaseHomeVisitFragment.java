package org.smartregister.chw.anc.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import org.json.JSONObject;

public class BaseHomeVisitFragment extends DialogFragment {

    protected JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getDialog() != null && getDialog().getWindow() != null) {
                    getDialog().getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                }
            }
        });
    }
}
