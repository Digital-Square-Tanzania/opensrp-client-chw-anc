package org.smartregister.chw.anc_sample.interactor;

import android.content.Context;

import org.smartregister.chw.anc.actionhelper.DangerSignsHelper;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncHomeVisitInteractor;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc_sample.R;
import org.smartregister.chw.anc_sample.activity.EntryActivity;
import org.smartregister.chw.anc_sample.utils.Constants;

import java.util.LinkedHashMap;

public class AncHomeVisitInteractor extends BaseAncHomeVisitInteractor {

    @Override
    public void calculateActions(final BaseAncHomeVisitContract.View view, final MemberObject memberObject, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            final LinkedHashMap<String, BaseAncHomeVisitAction> actionList = new LinkedHashMap<>();

            try {

                Context context = view.getContext();
                BaseAncHomeVisitAction danger_signs = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.anc_home_visit_danger_signs))
                        .withOptional(false)
                        .withFormName(Constants.HOME_VISIT_FORMS.DANGER_SIGNS)
                        .withHelper(new DangerSignsHelper())
                        .build();
                actionList.put(context.getString(R.string.anc_home_visit_danger_signs), danger_signs);

            } catch (BaseAncHomeVisitAction.ValidationException e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public MemberObject getMemberClient(String memberID) {
        return EntryActivity.getSampleMember();
    }
}
