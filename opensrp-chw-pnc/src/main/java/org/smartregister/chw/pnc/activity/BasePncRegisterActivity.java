package org.smartregister.chw.pnc.activity;

import org.smartregister.chw.anc.activity.BaseAncRegisterActivity;
import org.smartregister.chw.pnc.fragment.BasePncRegisterFragment;
import org.smartregister.view.fragment.BaseRegisterFragment;

public class BasePncRegisterActivity extends BaseAncRegisterActivity {

    @Override
    protected BaseRegisterFragment getRegisterFragment() {

        return new BasePncRegisterFragment();
    }



}
