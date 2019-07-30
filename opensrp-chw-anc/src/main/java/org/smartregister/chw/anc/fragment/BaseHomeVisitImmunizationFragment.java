package org.smartregister.chw.anc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.util.List;
import java.util.Map;

public class BaseHomeVisitImmunizationFragment extends BaseHomeVisitFragment {

    protected BaseAncHomeVisitContract.VisitView visitView;
    protected String baseEntityID;
    protected Map<String, List<VisitDetail>> details;

    public static BaseHomeVisitImmunizationFragment getInstance(final BaseAncHomeVisitContract.VisitView view, String baseEntityID, Map<String, List<VisitDetail>> details) {
        BaseHomeVisitImmunizationFragment fragment = new BaseHomeVisitImmunizationFragment();
        fragment.visitView = view;
        fragment.baseEntityID = baseEntityID;
        fragment.details = details;
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_visit_immunization, container, false);

        return view;
    }

}
