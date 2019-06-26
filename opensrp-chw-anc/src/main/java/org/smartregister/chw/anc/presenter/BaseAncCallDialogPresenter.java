package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.contract.BaseAncWomanCallDialogContract;

import java.lang.ref.WeakReference;

public class BaseAncCallDialogPresenter implements BaseAncWomanCallDialogContract.Presenter {
    private WeakReference<BaseAncWomanCallDialogContract.View> mView;

    public BaseAncCallDialogPresenter(BaseAncWomanCallDialogContract.View view) {
        mView = new WeakReference<>(view);

    }

    @Override
    public void initalize() {

    }
}
