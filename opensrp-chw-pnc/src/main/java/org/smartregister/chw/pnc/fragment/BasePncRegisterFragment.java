package org.smartregister.chw.pnc.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.chw.pnc.contract.BasePncRegisterFragmentContract;
import org.smartregister.chw.pnc.presenter.BasePncRegisterFragmentPresenter;
import org.smartregister.configurableviews.model.View;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Set;

import model.BasePncRegisterFragmentModel;
import provider.PncRegisterProvider;

public class BasePncRegisterFragment extends BaseRegisterFragment implements BasePncRegisterFragmentContract.View {
    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    public static final String CLICK_VIEW_DOSAGE_STATUS = "click_view_dosage_status";

    @Override
    public void initializeAdapter(Set<View> visibleColumns) {
        PncRegisterProvider pncRegisterProvider = new PncRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, pncRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    public void setupViews(android.view.View view) {
        super.setupViews(view);

        // Update top left icon
        qrCodeScanImageView = view.findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.scanQrCode);
        if (qrCodeScanImageView != null) {
            qrCodeScanImageView.setVisibility(android.view.View.GONE);
        }

        // Update Search bar
        android.view.View searchBarLayout = view.findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.search_bar_layout);
        searchBarLayout.setBackgroundResource(org.smartregister.chw.opensrp_chw_anc.R.color.customAppThemeBlue);

        if (getSearchView() != null) {
            getSearchView().setBackgroundResource(org.smartregister.chw.opensrp_chw_anc.R.color.white);
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(org.smartregister.chw.opensrp_chw_anc.R.drawable.ic_action_search, 0, 0, 0);
        }

        // Update sort filter
        TextView filterView = view.findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.filter_text_view);
        if (filterView != null) {
            filterView.setText(getString(org.smartregister.chw.opensrp_chw_anc.R.string.sort));
        }

        // Update title name
        ImageView logo = view.findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.opensrp_logo_image_view);
        if (logo != null) {
            logo.setVisibility(android.view.View.GONE);
        }

        CustomFontTextView titleView = view.findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.txt_title_label);
        if (titleView != null) {
            titleView.setVisibility(android.view.View.VISIBLE);
            titleView.setText(getString(org.smartregister.chw.pnc.R.string.pnc));
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }

    @Override
    public BasePncRegisterFragmentContract.Presenter presenter() {
        return (BasePncRegisterFragmentContract.Presenter) presenter;
    }

    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }
        presenter = new BasePncRegisterFragmentPresenter(this, new BasePncRegisterFragmentModel(), null);
    }

    @Override
    public void setUniqueID(String s) {
        if (getSearchView() != null) {
            getSearchView().setText(s);
        }
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {
//        implement search here
    }

    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }

    @Override
    protected void startRegistration() {
//        start forms here if the module requires registration
    }

    @Override
    public void showNotFoundPopup(String s) {
//        implement dialog
    }

    @Override
    protected void onViewClicked(android.view.View view) {
        if (getActivity() == null) {
            return;
        }

    }

}
