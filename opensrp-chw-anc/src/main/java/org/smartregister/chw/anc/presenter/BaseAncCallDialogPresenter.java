package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.contract.BaseAncWomanCallDialogContract;

import java.lang.ref.WeakReference;

public class BaseAncCallDialogPresenter implements BaseAncWomanCallDialogContract {

    public BaseAncCallDialogPresenter(BaseAncWomanCallDialogContract.View view) {
        WeakReference<BaseAncWomanCallDialogContract.View> mView = new WeakReference<>(view);

    }

}
