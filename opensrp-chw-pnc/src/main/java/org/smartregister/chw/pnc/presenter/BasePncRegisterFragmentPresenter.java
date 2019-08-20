package org.smartregister.chw.pnc.presenter;

import org.smartregister.chw.anc.contract.BaseAncRegisterFragmentContract;
import org.smartregister.chw.anc.presenter.BaseAncRegisterFragmentPresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.configurableviews.model.Field;

import java.util.List;

public class BasePncRegisterFragmentPresenter extends BaseAncRegisterFragmentPresenter {

    public BasePncRegisterFragmentPresenter(BaseAncRegisterFragmentContract.View view, BaseAncRegisterFragmentContract.Model model, String viewConfigurationIdentifier) {
        super(view, model, viewConfigurationIdentifier);
    }

    @Override
    public void updateSortAndFilter(List<Field> filterList, Field sortField) {
        // implement
    }

    @Override
    public String getMainCondition() {

        return "";
    }

    @Override
    public String getDefaultSortQuery() {
        return Constants.TABLES.PREGNANCY_OUTCOME + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ";
    }

    @Override
    public String getMainTable() {
        return Constants.TABLES.PREGNANCY_OUTCOME;
    }


    @Override
    public String getDueFilterCondition() {
        return "";
        /*
        return "(( " +
                "IFNULL(SUBSTR(" + DBConstants.KEY.LAST_HOME_VISIT + ",7,4) || SUBSTR(" + DBConstants.KEY.LAST_HOME_VISIT + ",4,2) || SUBSTR(" + DBConstants.KEY.LAST_HOME_VISIT + ",1,2) || '000000',0) " +
                "< STRFTIME('%Y%m%d%H%M%S', datetime('now','start of month')) " +
                "AND IFNULL(STRFTIME('%Y%m%d%H%M%S', datetime((" + DBConstants.KEY.VISIT_NOT_DONE + ")/1000,'unixepoch')),0) " +
                "< STRFTIME('%Y%m%d%H%M%S', datetime('now','start of month')) " +
                " ))";*/
    }

}
