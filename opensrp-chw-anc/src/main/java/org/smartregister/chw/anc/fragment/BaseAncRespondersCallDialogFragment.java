package org.smartregister.chw.anc.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.contract.BaseAncWomanCallDialogContract;
import org.smartregister.chw.anc.listener.BaseAncResponderCallWidgetDialogListener;
import org.smartregister.chw.opensrp_chw_anc.R;

import static org.smartregister.util.Utils.getName;

public class BaseAncRespondersCallDialogFragment extends BaseAncWomanCallDialogFragment {

    public static final String DIALOG_TAG = "BaseAncRespondersCallDialogFragment_DIALOG_TAG";
    private static String ancResponderName, ancResponderPhoneNumber;
    private View.OnClickListener listener = null;

    public static BaseAncRespondersCallDialogFragment launchDialog(Activity activity, String responderName, String responderPhone) {
        BaseAncRespondersCallDialogFragment dialogFragment = BaseAncRespondersCallDialogFragment.newInstance();
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        ancResponderPhoneNumber = responderPhone;
        ancResponderName = responderName;
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, DIALOG_TAG);
        return dialogFragment;
    }

    public static BaseAncRespondersCallDialogFragment newInstance() {
        return new BaseAncRespondersCallDialogFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.anc_responder_call_widget_dialog_fragment, container, false);
        setUpPosition();

        if (listener == null) {
            listener = new BaseAncResponderCallWidgetDialogListener(this);
        }

        initUI(dialogView);
        return dialogView;
    }

    private void initUI(ViewGroup rootView) {
        if (StringUtils.isNotBlank(ancResponderPhoneNumber)) {
            TextView ancWomanNameTextView = rootView.findViewById(R.id.responder_name);
            ancWomanNameTextView.setText(ancResponderName);
            TextView ancCallAncWomanPhone = rootView.findViewById(R.id.anc_call_responder_phone);
            ancCallAncWomanPhone.setTag(ancResponderPhoneNumber);
            ancCallAncWomanPhone.setText(getName(getCurrentContext().getString(R.string.anc_call), ancResponderPhoneNumber));
            ancCallAncWomanPhone.setOnClickListener(listener);
        }
        rootView.findViewById(R.id.anc_call_close).setOnClickListener(listener);
    }
}
