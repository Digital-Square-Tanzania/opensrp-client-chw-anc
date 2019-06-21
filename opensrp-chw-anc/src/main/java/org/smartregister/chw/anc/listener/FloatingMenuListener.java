package org.smartregister.chw.anc.listener;

import android.app.Activity;
import android.util.Log;

import org.smartregister.chw.anc.fragment.BaseAncWomanCallDialogFragment;

import java.lang.ref.WeakReference;

public class FloatingMenuListener implements OnClickFloatingMenu {
    private static String TAG = FloatingMenuListener.class.getCanonicalName();
    private WeakReference<Activity> context;
    private String familyBaseEntityId;

    private FloatingMenuListener(Activity context) {
        this.context = new WeakReference<>(context);
    }

    private static FloatingMenuListener instance;

    public static FloatingMenuListener getInstance(Activity context) {
        if (instance == null) {
            instance = new FloatingMenuListener(context);
        } else {
            if (instance.context.get() != context) {
                instance.context = new WeakReference<>(context);
            }
        }
        return instance;
    }



    @Override
    public void onClickMenu(int viewId) {
        if (context.get() != null) {

            if (context.get().isDestroyed()) {
                Log.d(TAG, "Activity Destroyed");
                return;
            }

            BaseAncWomanCallDialogFragment.launchDialog(context.get(), familyBaseEntityId);


        }
    }
}
