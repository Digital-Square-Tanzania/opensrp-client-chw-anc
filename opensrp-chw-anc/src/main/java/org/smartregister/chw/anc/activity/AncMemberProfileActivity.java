package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.model.AncMemberProfileModel;
import org.smartregister.chw.anc.presenter.AncMemberProfilePresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.domain.FetchStatus;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import timber.log.Timber;

public class AncMemberProfileActivity extends BaseProfileActivity implements View.OnClickListener, AncMemberProfileContract.View {
    private boolean isFromFamilyRegister = false;
    private TextView textViewTitle;
    private String childBaseEntityId;


    public static void startMe(Activity activity, String memberBaseEntityID) {
        Intent intent = new Intent(activity, AncMemberProfileActivity.class);
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, memberBaseEntityID);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_anc_member_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        textViewTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

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

//        setupViews();
        setUpToolbar();

    }

    @Override
    protected void onResumption() {
        Timber.v("Empty onResumption");
    }

    @Override
    public void onClick(View v) {
        Timber.v("Empty onClick");
    }

    private void setUpToolbar() {
        if (isFromFamilyRegister) {
            textViewTitle.setText(getString(R.string.return_to_family_members));
        } else {
            textViewTitle.setText(getString(R.string.return_to_all_anc_women));
        }

    }

    @Override
    protected void initializePresenter() {
        childBaseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        isFromFamilyRegister = getIntent().getBooleanExtra(Constants.INTENT_KEY.IS_FROM_FAMILY_REGISTER, false);
        String familyName =  "FAMILY_NAME";


        presenter = new AncMemberProfilePresenter(this, new AncMemberProfileModel(familyName), childBaseEntityId);
        fetchProfileData();


    }
    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }
    @Override
    protected void fetchProfileData() {


    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void startFormActivity(JSONObject form) {

    }

    @Override
    public void refreshProfile(FetchStatus fetchStatus) {

    }

    @Override
    public void displayShortToast(int resourceId) {

    }

    @Override
    public void setProfileImage(String baseEntityId) {

    }

    @Override
    public void setParentName(String parentName) {

    }

    @Override
    public void setGender(String gender) {

    }

    @Override
    public void setAddress(String address) {

    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setProfileName(String fullName) {

    }

    @Override
    public void setAge(String age) {

    }

    @Override
    public void setVisitButtonDueStatus() {

    }

    @Override
    public void setVisitButtonOverdueStatus() {

    }

    @Override
    public void setVisitNotDoneThisMonth() {

    }

    @Override
    public void setLastVisitRowView(String days) {

    }

    @Override
    public void setServiceNameDue(String name, String dueDate) {

    }

    @Override
    public void setServiceNameOverDue(String name, String dueDate) {

    }

    @Override
    public void setServiceNameUpcoming(String name, String dueDate) {

    }

    @Override
    public void setVisitLessTwentyFourView(String monthName) {

    }

    @Override
    public void setVisitAboveTwentyFourView() {

    }

    @Override
    public void setFamilyHasNothingDue() {

    }

    @Override
    public void setFamilyHasServiceDue() {

    }

    @Override
    public void setFamilyHasServiceOverdue() {

    }

    @Override
    public AncMemberProfileContract.Presenter presenter() {
        return null;
    }

    @Override
    public void updateHasPhone(boolean hasPhone) {

    }

    @Override
    public void enableEdit(boolean enable) {

    }

    @Override
    public void hideProgressBar() {

    }
}
