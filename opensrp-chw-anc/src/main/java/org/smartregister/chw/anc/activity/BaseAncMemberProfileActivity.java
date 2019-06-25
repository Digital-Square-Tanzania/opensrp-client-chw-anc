package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.presenter.BaseAncMemberProfilePresenter;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import timber.log.Timber;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;

public class BaseAncMemberProfileActivity extends BaseProfileActivity implements AncMemberProfileContract.View {
    protected MemberObject MEMBER_OBJECT;
    protected TextView text_view_anc_member_name, text_view_ga, text_view_address, text_view_id, textview_record_anc_visit;
    protected View view_anc_record;
    protected RelativeLayout rlLastVisit, rlUpcomingServices, rlFamilyServicesDue;

    public static void startMe(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, BaseAncMemberProfileActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    protected void registerPresenter() {
        presenter = new BaseAncMemberProfilePresenter(this, MEMBER_OBJECT);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_anc_member_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            MEMBER_OBJECT = (MemberObject) getIntent().getSerializableExtra(MEMBER_PROFILE_OBJECT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        appBarLayout = findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
        imageRenderHelper = new ImageRenderHelper(this);

        initializePresenter();

        setupViews();

    }

    @Override
    protected void setupViews() {
        text_view_anc_member_name = findViewById(R.id.text_view_anc_member_name);
        text_view_ga = findViewById(R.id.text_view_ga);
        text_view_address = findViewById(R.id.text_view_address);
        text_view_id = findViewById(R.id.text_view_id);
        textview_record_anc_visit = findViewById(R.id.textview_record_anc_visit);
        view_anc_record = findViewById(R.id.view_anc_record);

        rlLastVisit = findViewById(R.id.rlLastVisit);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);

        textview_record_anc_visit.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);
        rlUpcomingServices.setOnClickListener(this);
        rlFamilyServicesDue.setOnClickListener(this);
    }

    @Override
    protected void onResumption() {
        Timber.v("Empty onResumption");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlLastVisit) {
            this.openMedicalHistory();
        } else if (v.getId() == R.id.rlUpcomingServices) {
            this.openUpcomingService();
        } else if (v.getId() == R.id.rlFamilyServicesDue) {
            this.openFamilyDueServices();
        }
    }


    @Override
    protected void initializePresenter() {
        registerPresenter();
        fetchProfileData();
    }

    @Override
    public void setMemberName(String memberName) {
        text_view_anc_member_name.setText(memberName);
    }

    @Override
    public void setMemberGA(String memberGA) {
        String gest_age = String.format(getString(R.string.gest_age), String.valueOf(memberGA)) + " " + getString(R.string.gest_age_weeks);
        text_view_ga.setText(gest_age);
    }

    @Override
    public void setMemberAddress(String memberAddress) {
        text_view_address.setText(memberAddress);
    }

    public void setMemberChwMemberId(String memberChwMemberId) {
        String uniqueId = String.format(getString(R.string.unique_id_text), memberChwMemberId);
        text_view_id.setText(uniqueId);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        presenter().fetchProfileData();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public AncMemberProfileContract.Presenter presenter() {
        return (AncMemberProfileContract.Presenter) presenter;
    }

    @Override
    public void openMedicalHistory() {
        BaseAncMedicalHistory.startMe(this, MEMBER_OBJECT);
    }

    @Override
    public void openUpcomingService() {
        // TODO implement
    }

    @Override
    public void openFamilyDueServices() {
        // TODO implement
    }

}
