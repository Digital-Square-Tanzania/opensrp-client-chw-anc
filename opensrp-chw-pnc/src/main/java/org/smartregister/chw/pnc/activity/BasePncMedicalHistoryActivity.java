package org.smartregister.chw.pnc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import org.smartregister.chw.anc.activity.BaseAncMedicalHistoryActivity;
import org.smartregister.chw.anc.domain.GroupedVisit;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.pnc.contract.BasePncMedicalHistoryContract;
import org.smartregister.chw.pnc.interactor.BasePncMedicalHistoryInteractor;
import org.smartregister.chw.pnc.interactor.BasePncMedicalHistoryPresenter;

import java.util.List;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;

public class BasePncMedicalHistoryActivity extends BaseAncMedicalHistoryActivity implements BasePncMedicalHistoryContract.View {

    protected BasePncMedicalHistoryContract.Presenter pncPresenter;

    public static void startMe(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, BasePncMedicalHistoryActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    public void initializePncPresenter() {
        pncPresenter = new BasePncMedicalHistoryPresenter(new BasePncMedicalHistoryInteractor(), this, memberObject.getBaseEntityId());
    }

    @Override
    public BasePncMedicalHistoryContract.Presenter getPncPresenter() {
        return pncPresenter;
    }

    @Override
    public void onGroupedDataReceived(List<GroupedVisit> groupedVisits) {
        View view = renderMedicalHistoryView(groupedVisits);
        linearLayout.addView(view, 0);
    }

    @Override
    public View renderMedicalHistoryView(List<GroupedVisit> groupedVisits) {
        LayoutInflater inflater = getLayoutInflater();
        return inflater.inflate(org.smartregister.chw.opensrp_chw_anc.R.layout.medical_history_details, null);
    }
}
