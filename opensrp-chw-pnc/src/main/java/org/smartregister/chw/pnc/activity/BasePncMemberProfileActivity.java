package org.smartregister.chw.pnc.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.interactor.BasePncMemberProfileInteractor;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.text.MessageFormat;
import java.util.Date;

import static org.smartregister.util.Utils.getName;

public class BasePncMemberProfileActivity extends BaseAncMemberProfileActivity {
    private BasePncMemberProfileInteractor basePncMemberProfileInteractor = new BasePncMemberProfileInteractor();

    public static void startMe(Activity activity, String baseEntityID) {
        Intent intent = new Intent(activity, BasePncMemberProfileActivity.class);
        intent.putExtra(Constants.ANC_MEMBER_OBJECTS.BASE_ENTITY_ID, baseEntityID);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_HOME_VISIT);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        CustomFontTextView titleView = findViewById(R.id.toolbar_title);
        String titleText = TextUtils.isEmpty(getTitleViewText()) ? getString(R.string.return_to_all_pnc_women) : getTitleViewText();
        titleView.setText(titleText);
        record_reccuringvisit_done_bar.setVisibility(View.GONE);
        textViewAncVisitNot.setVisibility(View.GONE);
    }

    @Override
    public void setMemberName(String memberName) {
        basePncMemberProfileInteractor.getPncMotherNameDetails(memberObject, text_view_anc_member_name, imageView);
    }

    @Override
    public void setMemberGA(String memberGA) {
        String pncDay = basePncMemberProfileInteractor.getPncDay(memberObject.getBaseEntityId());
        if (pncDay != null) {
            text_view_ga.setText(getName(getString(R.string.pnc_day), pncDay));
        }
    }

    @Override
    public void setProfileImage(String baseEntityId, String entityType) {
        String pncDay = basePncMemberProfileInteractor.getPncDay(memberObject.getBaseEntityId());
        if (StringUtils.isNotBlank(pncDay) && Integer.parseInt(pncDay) >= 29) {
            imageRenderHelper.refreshProfileImage(baseEntityId, imageView, NCUtils.getPncMemberProfileImageResourceIdentifier());
        } else {
            imageRenderHelper.refreshProfileImage(baseEntityId, imageView, R.drawable.pnc_less_twenty_nine_days);
        }
    }

    @Override
    protected String getProfileType() {
        return Constants.MEMBER_PROFILE_TYPES.PNC;
    }

    @Override
    public void setRecordVisitTitle(String title) {
        textview_record_anc_visit.setText(getString(R.string.record_pnc_visit));
    }

    @Override
    public void openMedicalHistory() {
        BasePncMedicalHistoryActivity.startMe(this, memberObject);
    }

    @Override
    public void setLastVisit(Date lastVisitDate) {
        view_last_visit_row.setVisibility(View.VISIBLE);
        if (basePncMemberProfileInteractor.getLastVisitDate(memberObject.getBaseEntityId()) != null) {
            rlLastVisit.setVisibility(View.VISIBLE);
            String x = basePncMemberProfileInteractor.getLastVisitDate(memberObject.getBaseEntityId());
            tvLastVisitDate.setText(MessageFormat.format(getString(R.string.pnc_last_visit_text), x));
        }
    }

}
