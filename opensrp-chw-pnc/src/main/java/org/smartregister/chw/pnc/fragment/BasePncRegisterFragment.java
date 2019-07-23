package org.smartregister.chw.pnc.fragment;

import org.smartregister.chw.anc.fragment.BaseAncRegisterFragment;
import org.smartregister.chw.anc.model.BaseAncRegisterFragmentModel;
import org.smartregister.chw.pnc.presenter.BasePncRegisterFragmentPresenter;
import org.smartregister.configurableviews.model.View;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.util.Set;

import provider.PncRegisterProvider;

public class BasePncRegisterFragment extends BaseAncRegisterFragment {

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

        CustomFontTextView titleView = view.findViewById(org.smartregister.chw.opensrp_chw_anc.R.id.txt_title_label);
        if (titleView != null) {
            titleView.setText(getString(org.smartregister.chw.pnc.R.string.pnc));

        }
    }


    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }
        presenter = new BasePncRegisterFragmentPresenter(this, new BaseAncRegisterFragmentModel(), null);
    }

}
