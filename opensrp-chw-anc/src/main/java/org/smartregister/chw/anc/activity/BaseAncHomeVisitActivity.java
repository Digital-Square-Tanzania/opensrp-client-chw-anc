package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
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

    private static final String TAG = BaseAncHomeVisitActivity.class.getCanonicalName();

    private RecyclerView.Adapter mAdapter;
    protected Map<String, BaseAncHomeVisitAction> actionList = new LinkedHashMap<>();
    private ProgressBar progressBar;
    private TextView tvSubmit;
    private TextView tvTitle;
    protected BaseAncHomeVisitContract.Presenter presenter;
    protected String BASE_ENTITY_ID;
    private String current_action;

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
        displayProgressBar(true);
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

        mAdapter = new BaseAncHomeVisitAdapter(this, this, (LinkedHashMap) actionList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        redrawVisitUI();
    }

    protected void registerPresenter() {
        presenter = new BaseAncHomeVisitPresenter(BASE_ENTITY_ID, this, new BaseAncHomeVisitInteractor());
    }

    @Override
    public void initializeActions(LinkedHashMap<String, BaseAncHomeVisitAction> map) {
        for (Map.Entry<String, BaseAncHomeVisitAction> entry : map.entrySet()) {
            actionList.put(entry.getKey(), entry.getValue());
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        displayProgressBar(false);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
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
        return presenter;
    }

    @Override
    public Form getFormConfig() {
        return null;
    }

    @Override
    public void startForm(BaseAncHomeVisitAction ancHomeVisitAction) {
        current_action = ancHomeVisitAction.getTitle();

        if (StringUtils.isNotBlank(ancHomeVisitAction.getJsonPayload())) {
            try {
                JSONObject jsonObject = new JSONObject(ancHomeVisitAction.getJsonPayload());
                startFormActivity(jsonObject);
            } catch (Exception e) {
                Timber.e(e);
                String locationId = AncLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
                presenter().startForm(ancHomeVisitAction.getFormName(), BASE_ENTITY_ID, locationId);
            }
        } else {
            String locationId = AncLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
            presenter().startForm(ancHomeVisitAction.getFormName(), BASE_ENTITY_ID, locationId);
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, JsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void startFragment(BaseAncHomeVisitAction ancHomeVisitAction) {
        current_action = ancHomeVisitAction.getTitle();

        if (ancHomeVisitAction.getDestinationFragment() != null) {
            ancHomeVisitAction.getDestinationFragment().show(getFragmentManager(), TAG);
        }
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
    public void onDialogOptionUpdated(String jsonString) {
        BaseAncHomeVisitAction ancHomeVisitAction = actionList.get(current_action);
        if (ancHomeVisitAction != null) {
            ancHomeVisitAction.setJsonPayload(jsonString);
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                    BaseAncHomeVisitAction ancHomeVisitAction = actionList.get(current_action);
                    if (ancHomeVisitAction != null) {
                        ancHomeVisitAction.setJsonPayload(jsonString);
                    }
                } catch (Exception e) {
                    Timber.e(Log.getStackTraceString(e));
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {

                BaseAncHomeVisitAction ancHomeVisitAction = actionList.get(current_action);
                if (ancHomeVisitAction != null)
                    ancHomeVisitAction.evaluateStatus();
            }

        }

        // update the adapter after every payload
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
