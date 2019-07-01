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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.ei.drishti.dto.AlertStatus;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.custom_views.BaseAncFloatingMenu;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncMemberProfileInteractor;
import org.smartregister.chw.anc.presenter.BaseAncMemberProfilePresenter;
import org.smartregister.chw.anc.util.Util;
import org.smartregister.chw.anc.util.Utils;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.FAMILY_HEAD_NAME;
import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.FAMILY_HEAD_PHONE;
import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;
import static org.smartregister.util.Utils.getName;

public class BaseAncMemberProfileActivity extends BaseProfileActivity implements BaseAncMemberProfileContract.View {
    protected MemberObject MEMBER_OBJECT;
    protected TextView text_view_anc_member_name, text_view_ga, text_view_address, text_view_id, textview_record_anc_visit, textViewAncVisitNot, textViewNotVisitMonth, textViewUndo;
    private LinearLayout layoutRecordView;
    protected RelativeLayout rlLastVisit, rlUpcomingServices, rlFamilyServicesDue, layoutRecordButtonDone, layoutNotRecordView;
    private String familyHeadName;
    private String familyHeadPhoneNumber;
    private BaseAncFloatingMenu baseAncFloatingMenu;
    private ImageView imageViewCross;
    protected View view_anc_record, view_last_visit_row, view_most_due_overdue_row, view_family_row;
    private TextView tvLastVisitDate, tvUpComingServices, tvFamilyStatus;
    private ProgressBar progressBar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

    private CircleImageView imageView;


    public static void startMe(Activity activity, MemberObject memberObject, String familyHeadName, String familyHeadPhoneNumber) {
        Intent intent = new Intent(activity, BaseAncMemberProfileActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        intent.putExtra(FAMILY_HEAD_NAME, familyHeadName);
        intent.putExtra(FAMILY_HEAD_PHONE, familyHeadPhoneNumber);
        activity.startActivity(intent);
    }

    protected void registerPresenter() {
        presenter = new BaseAncMemberProfilePresenter(this, new BaseAncMemberProfileInteractor(), MEMBER_OBJECT);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_anc_member_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            MEMBER_OBJECT = (MemberObject) getIntent().getSerializableExtra(MEMBER_PROFILE_OBJECT);
            familyHeadName = getIntent().getStringExtra(FAMILY_HEAD_NAME);
            familyHeadPhoneNumber = getIntent().getStringExtra(FAMILY_HEAD_PHONE);
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

        progressBar = findViewById(R.id.progress_bar);
        view_last_visit_row = findViewById(R.id.view_last_visit_row);
        view_most_due_overdue_row = findViewById(R.id.view_most_due_overdue_row);
        view_family_row = findViewById(R.id.view_family_row);

        tvLastVisitDate = findViewById(R.id.textview_last_vist_day);
        tvUpComingServices = findViewById(R.id.textview_name_due);
        tvFamilyStatus = findViewById(R.id.textview_family_has);

        initializePresenter();
        setupViews();
    }

    @Override
    protected void setupViews() {
        String ancWomanName = getName(MEMBER_OBJECT.getFirstName(), MEMBER_OBJECT.getMiddleName());
        ancWomanName = getName(ancWomanName, MEMBER_OBJECT.getMiddleName());

        if (StringUtils.isNotBlank(MEMBER_OBJECT.getPhoneNumber()) || StringUtils.isNotBlank(familyHeadPhoneNumber)) {
            baseAncFloatingMenu = new BaseAncFloatingMenu(this, ancWomanName, MEMBER_OBJECT.getPhoneNumber(), familyHeadName, familyHeadPhoneNumber);
            baseAncFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseAncFloatingMenu, linearLayoutParams);
        }
        text_view_anc_member_name = findViewById(R.id.text_view_anc_member_name);
        text_view_ga = findViewById(R.id.text_view_ga);
        text_view_address = findViewById(R.id.text_view_address);
        text_view_id = findViewById(R.id.text_view_id);
        textview_record_anc_visit = findViewById(R.id.textview_record_anc_visit);
        view_anc_record = findViewById(R.id.view_anc_record);
        layoutRecordView = findViewById(R.id.record_visit_bar);
        textViewNotVisitMonth = findViewById(R.id.textview_not_visit_this_month);


        rlLastVisit = findViewById(R.id.rlLastVisit);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);

        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);
        textViewAncVisitNot = findViewById(R.id.textview_anc_visit_not);
        layoutRecordButtonDone = findViewById(R.id.record_visit_done_bar);
        textViewUndo = findViewById(R.id.textview_undo);
        imageViewCross = findViewById(R.id.cross_image);
        layoutNotRecordView = findViewById(R.id.record_visit_status_bar);


        textview_record_anc_visit.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);
        rlUpcomingServices.setOnClickListener(this);
        rlFamilyServicesDue.setOnClickListener(this);

        textViewAncVisitNot.setOnClickListener(this);
        textViewUndo.setOnClickListener(this);
        imageViewCross.setOnClickListener(this);
        layoutRecordButtonDone.setOnClickListener(this);


        imageView = findViewById(R.id.imageview_profile);
        imageView.setBorderWidth(2);


    }

    @Override
    public void setVisitNotDoneThisMonth() {
        openVisitMonthView();
        textViewNotVisitMonth.setText(getString(R.string.not_visiting_this_month));
        textViewUndo.setText(getString(R.string.undo));
        textViewUndo.setVisibility(View.VISIBLE);
        imageViewCross.setImageResource(R.drawable.activityrow_notvisited);
    }

    @Override
    public void updateVisitNotDone(long value) {
        textViewUndo.setVisibility(View.GONE);

        layoutNotRecordView.setVisibility(View.GONE);
        layoutRecordButtonDone.setVisibility(View.VISIBLE);
        layoutRecordView.setVisibility(View.VISIBLE);
    }


    public void openVisitMonthView() {
        layoutNotRecordView.setVisibility(View.VISIBLE);
        layoutRecordButtonDone.setVisibility(View.GONE);
        layoutRecordView.setVisibility(View.GONE);

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
        } else if (v.getId() == R.id.textview_anc_visit_not) {
            presenter().getView().setVisitNotDoneThisMonth();
        } else if (v.getId() == R.id.textview_undo) {
            presenter().getView().updateVisitNotDone(0);
        }

    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        registerPresenter();
        fetchProfileData();
        presenter().refreshProfileBottom();
    }

    @Override
    public void setProfileImage(String baseEntityId, String entityType) {
        imageRenderHelper.refreshProfileImage(baseEntityId, imageView, Util.getMemberProfileImageResourceIDentifier(entityType));
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLastVisit(Date lastVisitDate) {
        if (lastVisitDate == null)
            return;

        view_last_visit_row.setVisibility(View.VISIBLE);
        rlLastVisit.setVisibility(View.VISIBLE);

        int days = Days.daysBetween(new DateTime(lastVisitDate).toLocalDate(), new DateTime().toLocalDate()).getDays();
        tvLastVisitDate.setText(getString(R.string.last_visit_40_days_ago, String.valueOf(days)));
    }

    @Override
    public void setUpComingServicesStatus(String service, AlertStatus status, Date date) {
        if (status == AlertStatus.complete)
            return;

        view_most_due_overdue_row.setVisibility(View.VISIBLE);
        rlUpcomingServices.setVisibility(View.VISIBLE);

        if (status == AlertStatus.upcoming) {
            tvUpComingServices.setText(Utils.fromHtml(getString(R.string.vaccine_service_upcoming, service, dateFormat.format(date))));
        } else {
            tvUpComingServices.setText(Utils.fromHtml(getString(R.string.vaccine_service_due, service, dateFormat.format(date))));
        }
    }

    @Override
    public void setFamilyStatus(AlertStatus status) {
        view_family_row.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.VISIBLE);

        if (status == AlertStatus.complete) {
            tvFamilyStatus.setText(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            tvFamilyStatus.setText(getString(R.string.family_has_services_due));
        } else if (status == AlertStatus.urgent) {
            tvFamilyStatus.setText(Utils.fromHtml(getString(R.string.family_has_service_overdue)));
        }
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
        return this;
    }

    @Override
    public BaseAncMemberProfileContract.Presenter presenter() {
        return (BaseAncMemberProfileContract.Presenter) presenter;
    }

    @Override
    public void openMedicalHistory() {
        BaseAncMedicalHistoryActivity.startMe(this, MEMBER_OBJECT);
    }

    @Override
    public void openUpcomingService() {
        BaseAncUpcomingServicesActivity.startMe(this, MEMBER_OBJECT);
    }

    @Override
    public void openFamilyDueServices() {
        // TODO implement
    }

}
