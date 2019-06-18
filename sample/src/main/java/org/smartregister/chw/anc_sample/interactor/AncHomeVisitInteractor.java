package org.smartregister.chw.anc_sample.interactor;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONObject;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.chw.anc.interactor.BaseAncHomeVisitInteractor;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc_sample.utils.Constants;

import java.util.LinkedHashMap;

import timber.log.Timber;

public class AncHomeVisitInteractor extends BaseAncHomeVisitInteractor {

    @Override
    public void calculateActions(final BaseAncHomeVisitContract.View view, final String memberID, final BaseAncHomeVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final LinkedHashMap<String, BaseAncHomeVisitAction> actionList = new LinkedHashMap<>();

                try {

                    // sample form opening action
                    final BaseAncHomeVisitAction ds = new BaseAncHomeVisitAction("Danger Signs", "", false, null, Constants.FORMS.ANC_REGISTRATION);
                    actionList.put("Danger Signs", ds);

                    // sample action using json form configured payload
                    final BaseAncHomeVisitAction anc = new BaseAncHomeVisitAction("ANC Card Received", "", true,
                            BaseAncHomeVisitFragment.getInstance(view, Constants.HOME_VISIT_FORMS.ANC_CARD_FORM, null), null);
                    anc.setAncHomeVisitActionHelper(new BaseAncHomeVisitAction.AncHomeVisitActionHelper() {
                        @Override
                        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
                            if (anc.getJsonPayload() != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(anc.getJsonPayload());
                                    String value = jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.VALUE);

                                    if (value.equalsIgnoreCase("Yes")) {
                                        return BaseAncHomeVisitAction.Status.COMPLETED;
                                    } else if (value.equalsIgnoreCase("No")) {
                                        return BaseAncHomeVisitAction.Status.PARTIALLY_COMPLETED;
                                    } else {
                                        return BaseAncHomeVisitAction.Status.PENDING;
                                    }
                                } catch (Exception e) {
                                    Timber.e(e);
                                }
                            }
                            return anc.computedStatus();
                        }
                    });

                    actionList.put("ANC Card Received", anc);


                    actionList.put("TT Immunization 1", new BaseAncHomeVisitAction("TT Immunization 1", "", false,
                            BaseAncHomeVisitFragment.getInstance(view, Constants.HOME_VISIT_FORMS.IMMUNIZATION, null), null));

                } catch (BaseAncHomeVisitAction.ValidationException e) {
                    e.printStackTrace();
                }

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.preloadActions(actionList);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }
}
