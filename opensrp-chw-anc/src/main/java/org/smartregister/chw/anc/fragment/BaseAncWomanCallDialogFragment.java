package org.smartregister.chw.anc.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.contract.BaseAncWomanCallDialogContract;
import org.smartregister.chw.anc.listener.BaseAncWomanCallWidgetDialogListener;
import org.smartregister.chw.anc.presenter.BaseAncCallDialogPresenter;
import org.smartregister.chw.opensrp_chw_anc.R;

import static org.smartregister.util.Utils.getName;

public class BaseAncWomanCallDialogFragment extends DialogFragment implements BaseAncWomanCallDialogContract.View {

    public static final String DIALOG_TAG = "BaseAncCallWidgetDialogFragment_DIALOG_TAG";
    private static String ancWomanName, ancWomanPhoneNumber, ancFamillyHeadName, ancFamilyHeadPhone;
    private View.OnClickListener listener = null;
    private BaseAncWomanCallDialogContract.Dialer mDialer;

    public static BaseAncWomanCallDialogFragment launchDialog(Activity activity, String womanName, String ancWomanPhone, String familyHeadName, String familyHeadPhone) {
        BaseAncWomanCallDialogFragment dialogFragment = BaseAncWomanCallDialogFragment.newInstance();
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        ancWomanPhoneNumber = ancWomanPhone;
        ancWomanName = womanName;
        ancFamillyHeadName = familyHeadName;
        ancFamilyHeadPhone = familyHeadPhone;
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, DIALOG_TAG);

        return dialogFragment;
    }

    public static BaseAncWomanCallDialogFragment newInstance() {
        return new BaseAncWomanCallDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ChwTheme_Dialog_FullWidth);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.anc_member_call_widget_dialog_fragment, container, false);
        setUpPosition();

        if (listener == null) {
            listener = new BaseAncWomanCallWidgetDialogListener(this);
        }

        initUI(dialogView);
        initializePresenter();
        return dialogView;
    }

    private void initUI(ViewGroup rootView) {

        if (StringUtils.isNotBlank(ancWomanPhoneNumber)) {
            TextView ancWomanNameTextView = rootView.findViewById(R.id.call_anc_woman_name);
            ancWomanNameTextView.setText(ancWomanName);

            TextView ancCallAncWomanPhone = rootView.findViewById(R.id.call_anc_woman_phone);
            ancCallAncWomanPhone.setTag(ancWomanPhoneNumber);
            ancCallAncWomanPhone.setText(getName(getCurrentContext().getString(R.string.anc_call), ancWomanPhoneNumber));
            ancCallAncWomanPhone.setOnClickListener(listener);
        } else {

            rootView.findViewById(R.id.layout_anc_woman).setVisibility(android.view.View.GONE);
        }

        if (StringUtils.isNotBlank(ancFamilyHeadPhone)) {
            TextView familyHeadName = rootView.findViewById(R.id.anc_call_head_name);
            familyHeadName.setText(ancFamillyHeadName);

            TextView ancCallHeadPhone = rootView.findViewById(R.id.anc_call_head_phone);
            ancCallHeadPhone.setTag(ancFamilyHeadPhone);
            ancCallHeadPhone.setText(getName(getCurrentContext().getString(R.string.anc_call), ancFamilyHeadPhone));
            ancCallHeadPhone.setOnClickListener(listener);

        } else {

            rootView.findViewById(R.id.anc_layout_family_head).setVisibility(android.view.View.GONE);
        }

        rootView.findViewById(R.id.anc_call_close).setOnClickListener(listener);

    }

    private void setUpPosition() {
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.y = 20;
        getDialog().getWindow().setAttributes(p);
    }

    @Override
    public Context getCurrentContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext();
        } else {
            return getActivity().getApplicationContext();
        }
    }

    @Override
    public BaseAncWomanCallDialogContract.Dialer getPendingCallRequest() {
        return null;
    }

    @Override
    public void setPendingCallRequest(BaseAncWomanCallDialogContract.Dialer dialer) {
        mDialer = dialer;
    }

    @Override
    public BaseAncWomanCallDialogContract.Presenter initializePresenter() {
        return new BaseAncCallDialogPresenter(this);
    }

}
