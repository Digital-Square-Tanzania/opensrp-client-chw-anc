package org.smartregister.chw.anc.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.smartregister.chw.anc.adapter.BaseAncMedicalHistoryAdapter;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.model.BaseHomeVisitHistoricAction;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT;

public class BaseAncMedicalHistoryActivity extends AppCompatActivity {

    protected MemberObject memberObject;
    private TextView tvTitle;
    private RecyclerView.Adapter mAdapter;
    protected Map<String, List<BaseHomeVisitHistoricAction>> actionList = new LinkedHashMap<>();
    private ProgressBar progressBar;

    public static void startMe(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, BaseAncMedicalHistoryActivity.class);
        intent.putExtra(MEMBER_PROFILE_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_anc_medical_history);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            memberObject = (MemberObject) getIntent().getSerializableExtra(MEMBER_PROFILE_OBJECT);
        }
        getActionList();
        setUpActionBar();
        setUpView();
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbar_title);
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

    }

    public void setUpView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMedicalHistory);
        recyclerView.setHasFixedSize(false);
        progressBar = findViewById(R.id.progressBarMedicalHistory);
        progressBar.setVisibility(View.GONE);


        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.back_to, memberObject.getFullName()));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BaseAncMedicalHistoryAdapter(actionList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    // TODO remove and replace
    private void getActionList() {
        actionList.put("LAST VISIT", new ArrayList<BaseHomeVisitHistoricAction>());
        actionList.put("ANC CARD", new ArrayList<BaseHomeVisitHistoricAction>());
        actionList.put("ANC HEALTH FACILITY VISITS", new ArrayList<BaseHomeVisitHistoricAction>());
        actionList.put("TT IMMUNIZATIONS", new ArrayList<BaseHomeVisitHistoricAction>());
    }
}
