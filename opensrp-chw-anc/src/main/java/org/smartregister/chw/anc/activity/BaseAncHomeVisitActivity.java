package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vijay.jsonwizard.domain.Form;

import org.smartregister.AllConstants;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.adapter.BaseAncHomeVisitAdapter;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.interactor.BaseAncHomeVisitInteractor;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.presenter.BaseAncHomeVisitPresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.view.activity.SecuredActivity;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import timber.log.Timber;

public class BaseAncHomeVisitActivity extends SecuredActivity implements BaseAncHomeVisitContract.View, View.OnClickListener {

    private RecyclerView.Adapter mAdapter;
    protected Map<String, BaseAncHomeVisitAction> actionList = new LinkedHashMap<>();
    private ProgressBar progressBar;
    private TextView tvSubmit;
    private TextView tvTitle;
    private BaseAncHomeVisitContract.Presenter presenter;
    private String BASE_ENTITY_ID;

    public static void startMe(Activity activity, String memberBaseEntityID) {
        Intent intent = new Intent(activity, BaseAncHomeVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, memberBaseEntityID);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_anc_homevisit);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            BASE_ENTITY_ID = extras.getString(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        }

        setUpView();
        registerPresenter();
    }

    public void setUpView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.close).setOnClickListener(this);
        tvSubmit = findViewById(R.id.customFontTextViewSubmit);
        tvSubmit.setOnClickListener(this);
        tvTitle = findViewById(R.id.customFontTextViewName);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            initializeActions();
        } catch (BaseAncHomeVisitAction.ValidationException e) {
            Timber.e(e);
        }
        mAdapter = new BaseAncHomeVisitAdapter(this, this, (LinkedHashMap) actionList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        redrawVisitUI();
    }

    protected void registerPresenter() {
        presenter = new BaseAncHomeVisitPresenter(BASE_ENTITY_ID, this, new BaseAncHomeVisitInteractor());
    }

    /**
     * initializes the action list.
     * Implement new actions on the display.
     * This function can be pre-populated by fragments
     */
    protected void initializeActions() throws BaseAncHomeVisitAction.ValidationException {
        actionList.put("Danger Signs", new BaseAncHomeVisitAction("Danger Signs", "None", false, null, "ds"));
        actionList.put("ANC Counseling", new BaseAncHomeVisitAction("ANC Counseling", "", false, null, "anc"));
        actionList.put("Sleeping under a LLITN", new BaseAncHomeVisitAction("Sleeping under a LLITN", "", false, null, "anc"));
        actionList.put("ANC Card Received", new BaseAncHomeVisitAction("ANC Card Received", "", false, null, "anc"));
        actionList.put("ANC Health Facility Visit 1", new BaseAncHomeVisitAction("ANC Health Facility Visit 1", "", false, null, "anc"));
        actionList.put("TT Immunization 1", new BaseAncHomeVisitAction("TT Immunization 1", "", false, null, "anc"));
        actionList.put("IPTp-SP dose 1", new BaseAncHomeVisitAction("IPTp-SP dose 1", "", false, null, "anc"));
        actionList.put("Observation & Illness", new BaseAncHomeVisitAction("Observation & Illness", "", true, null, "anc"));
    }

    @Override
    protected void onCreation() {
        Timber.v("Empty onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("Empty onResumption");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close) {
            close();
        } else if (v.getId() == R.id.customFontTextViewSubmit) {
            submitVisit();
        }
    }

    @Override
    public BaseAncHomeVisitContract.Presenter presenter() {
        return null;
    }

    @Override
    public Form getFormConfig() {
        return null;
    }

    @Override
    public void startFrom(String formName) {
        String locationId = AncLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
        presenter().startForm(formName, BASE_ENTITY_ID, locationId);
    }

    @Override
    public void startFragment(Fragment fragment) {
        Timber.v("startFragment");
    }

    @Override
    public void redrawHeader(String memberName, String age) {
        tvTitle.setText(MessageFormat.format("{0}, {1} - {2}", memberName, age, getString(R.string.anc_visit)));
    }

    @Override
    public void redrawVisitUI() {
        boolean valid = true;
        for (Map.Entry<String, BaseAncHomeVisitAction> entry : actionList.entrySet()) {
            if (!entry.getValue().isOptional() && entry.getValue().getActionStatus() == BaseAncHomeVisitAction.Status.PENDING) {
                valid = false;
                break;
            }
        }

        int res_color = valid ? R.color.white : R.color.light_grey;
        tvSubmit.setTextColor(getResources().getColor(res_color));

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    @Override
    public Map<String, BaseAncHomeVisitAction> getAncHomeVisitActions() {
        return actionList;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public BaseAncHomeVisitContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void submitVisit() {
        Timber.v("submitVisit");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
