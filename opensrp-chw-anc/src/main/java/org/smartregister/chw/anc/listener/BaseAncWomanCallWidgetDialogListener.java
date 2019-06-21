package org.smartregister.chw.anc.listener;

import android.view.View;

import org.smartregister.chw.anc.fragment.BaseAncWomanCallDialogFragment;

public class BaseAncWomanCallWidgetDialogListener implements View.OnClickListener {

    private static String TAG = BaseAncWomanCallWidgetDialogListener.class.getCanonicalName();

    private BaseAncWomanCallDialogFragment callDialogFragment;

    public BaseAncWomanCallWidgetDialogListener(BaseAncWomanCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {

    }

}
