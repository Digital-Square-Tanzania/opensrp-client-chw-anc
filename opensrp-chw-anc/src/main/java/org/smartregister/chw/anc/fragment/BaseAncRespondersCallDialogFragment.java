package org.smartregister.chw.anc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.listener.BaseAncResponderCallWidgetDialogListener;
import org.smartregister.chw.opensrp_chw_anc.R;

import static org.smartregister.util.Utils.getName;

public class BaseAncRespondersCallDialogFragment extends BaseAncWomanCallDialogFragment {

    public static final String DIALOG_TAG = "BaseAncRespondersCallDialogFragment_DIALOG_TAG";
    private static String ancResponderName;
    private static String ancResponderPhoneNumber;
    private static String ancFacilityName;
    private static String ancFacilityType;
    private static boolean basicServicesProvided;
    private static boolean cEmONC;
    private static String ownership;
    private static boolean isResponder;
    private View.OnClickListener listener = null;

    public static BaseAncRespondersCallDialogFragment launchDialog(Activity activity, String responderName, String responderPhone, boolean isAncResponder, String facilityName, String facilityType,
                                                                   boolean servicesProvided, boolean ancCEmONC, String ancOwnership) {
        BaseAncRespondersCallDialogFragment dialogFragment = BaseAncRespondersCallDialogFragment.newInstance();
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        ancResponderPhoneNumber = responderPhone;
        ancResponderName = responderName;
        isResponder = isAncResponder;
        ancFacilityName = facilityName;
        ancFacilityType = facilityType;
        basicServicesProvided = servicesProvided;
        ownership = ancOwnership;
        cEmONC = ancCEmONC;
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
        if (isResponder && StringUtils.isNotBlank(ancResponderPhoneNumber)) {
            TextView ancResponderNameTextView = rootView.findViewById(R.id.responder_name);
            ancResponderNameTextView.setText(ancResponderName);
            TextView ancCallResponderPhone = rootView.findViewById(R.id.anc_call_responder_phone);
            ancCallResponderPhone.setTag(ancResponderPhoneNumber);
            ancCallResponderPhone.setText(getName(getCurrentContext().getString(R.string.anc_call), ancResponderPhoneNumber));
            ancCallResponderPhone.setOnClickListener(listener);
        } else if (!isResponder) {
            rootView.findViewById(R.id.call_responder_title).setVisibility(View.GONE);
            TextView callTitle = rootView.findViewById(R.id.call_title);
            TextView facilityNameTextView = rootView.findViewById(R.id.responder_name);
            TextView basicServiceProvidedTextView = rootView.findViewById(R.id.basic_services_provided);
            TextView cEmONCTextView = rootView.findViewById(R.id.cEmONCView);
            callTitle.setText(getName(ancFacilityName, ancFacilityType));
            facilityNameTextView.setText(String.format(getString(R.string.facility_ownership), ownership));
            basicServiceProvidedTextView.setText(String.format(getString(R.string.basic_services_provided), basicServicesProvided ? getString(R.string.anc_yes) : getString(R.string.anc_no)));
            basicServiceProvidedTextView.setVisibility(View.VISIBLE);
            cEmONCTextView.setText(String.format(getString(R.string.cEmONC), cEmONC ? getString(R.string.anc_yes) : getString(R.string.anc_no)));
            cEmONCTextView.setVisibility(View.VISIBLE);

        }
        rootView.findViewById(R.id.anc_call_close).setOnClickListener(listener);
    }
}
