package org.smartregister.chw.pnc.activity;

import org.smartregister.chw.anc.activity.BaseAncRegisterActivity;
import org.smartregister.chw.pnc.fragment.BasePncRegisterFragment;

public class BasePncRegisterActivity extends BaseAncRegisterActivity {

    @Override
    protected BasePncRegisterFragment getRegisterFragment() {
        return new BasePncRegisterFragment();
    }

}
