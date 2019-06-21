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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.smartregister.chw.anc.contract.BaseAncWomanCallDialogContract;
import org.smartregister.chw.anc.listener.BaseAncWomanCallWidgetDialogListener;
import org.smartregister.chw.anc.presenter.BaseAncCallDialogPresenter;
import org.smartregister.chw.opensrp_chw_anc.R;


public class BaseAncWomanCallDialogFragment extends DialogFragment implements BaseAncWomanCallDialogContract.View {


    public static final String DIALOG_TAG = "BaseAncCallWidgetDialogFragment_DIALOG_TAG";

    private View.OnClickListener listener = null;
    private BaseAncWomanCallDialogContract.Dialer mDialer;
    private String familyBaseEntityId;
    private LinearLayout llFamilyHead;
    private TextView tvFamilyHeadTitle;
    private TextView tvFamilyHeadName;
    private TextView tvFamilyHeadPhone;
    private LinearLayout llCareGiver;
    private TextView tvCareGiverTitle;
    private TextView tvCareGiverName;
    private TextView tvCareGiverPhone;

    public static BaseAncWomanCallDialogFragment launchDialog(Activity activity,
                                                              String familyBaseEntityId) {
        BaseAncWomanCallDialogFragment dialogFragment = BaseAncWomanCallDialogFragment.newInstance(familyBaseEntityId);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, DIALOG_TAG);

        return dialogFragment;
    }

    public static BaseAncWomanCallDialogFragment newInstance(String familyBaseEntityId) {
        BaseAncWomanCallDialogFragment familyCallDialogFragment = new BaseAncWomanCallDialogFragment();
        familyCallDialogFragment.setFamilyBaseEntityId(familyBaseEntityId);
        return familyCallDialogFragment;
    }

    protected void setFamilyBaseEntityId(String familyBaseEntityId) {
        this.familyBaseEntityId = familyBaseEntityId;
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

        llFamilyHead = rootView.findViewById(R.id.layout_family_head);
        tvFamilyHeadTitle = rootView.findViewById(R.id.call_head_title);
        tvFamilyHeadName = rootView.findViewById(R.id.call_head_name);
        tvFamilyHeadPhone = rootView.findViewById(R.id.call_head_phone);

        llCareGiver = rootView.findViewById(R.id.layout_caregiver);
        tvCareGiverTitle = rootView.findViewById(R.id.call_caregiver_title);
        tvCareGiverName = rootView.findViewById(R.id.call_caregiver_name);
        tvCareGiverPhone = rootView.findViewById(R.id.call_caregiver_phone);

        rootView.findViewById(R.id.close).setOnClickListener(listener);
        tvFamilyHeadPhone.setOnClickListener(listener);
        tvCareGiverPhone.setOnClickListener(listener);
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
