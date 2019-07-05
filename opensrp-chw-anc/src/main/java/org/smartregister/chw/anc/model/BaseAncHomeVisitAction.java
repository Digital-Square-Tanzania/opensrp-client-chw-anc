package org.smartregister.chw.anc.model;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This action list allows users to either load a form or link it to a separate fragment.
 */
public class BaseAncHomeVisitAction {

    private String title;
    private String subTitle;
    private Status actionStatus = Status.PENDING;
    private ScheduleStatus scheduleStatus = ScheduleStatus.DUE;
    private boolean optional = true;
    private BaseAncHomeVisitFragment destinationFragment;
    private String formName;
    private String jsonPayload;
    private String selectedOption;
    private AncHomeVisitActionHelper ancHomeVisitActionHelper;
    private VaccineWrapper vaccineWrapper;
    private ServiceWrapper serviceWrapper;
    private Map<String, List<VisitDetail>> details = new HashMap<>();
    private Context context;

    private BaseAncHomeVisitAction() {
    }

    public static class Builder {
        private BaseAncHomeVisitAction action;

        public Builder(Context context, String title) {
            action = new BaseAncHomeVisitAction();
            action.context = context;
            action.title = title;
        }

        public Builder withSubtitle(String subTitle) {
            action.subTitle = subTitle;
            return this;
        }

        public Builder withOptional(boolean optional) {
            action.optional = optional;
            return this;
        }

        public Builder withDestinationFragment(BaseAncHomeVisitFragment destinationFragment) {
            action.destinationFragment = destinationFragment;
            return this;
        }

        public Builder withFormName(String formName) {
            action.formName = formName;
            return this;
        }

        public Builder withDetails(Map<String, List<VisitDetail>> details) {
            action.details = details;
            return this;
        }

        public Builder withHelper(AncHomeVisitActionHelper ancHomeVisitActionHelper) {
            action.ancHomeVisitActionHelper = ancHomeVisitActionHelper;
            return this;
        }

        public Builder withScheduleStatus(ScheduleStatus scheduleStatus) {
            action.scheduleStatus = scheduleStatus;
            return this;
        }

        public Builder withVaccineWrapper(VaccineWrapper vaccineWrapper) {
            action.vaccineWrapper = vaccineWrapper;
            return this;
        }

        public Builder withServiceWrapper(ServiceWrapper serviceWrapper) {
            action.serviceWrapper = serviceWrapper;
            return this;
        }

        public BaseAncHomeVisitAction build() throws ValidationException {
            action.validateMe();
            action.initialize();
            return action;
        }
    }

    private void initialize() {
        try {
            if (StringUtils.isBlank(jsonPayload) && StringUtils.isNotBlank(formName)) {
                JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);

                // update the form details
                if (details.size() > 0) {
                    JsonFormUtils.populateForm(jsonObject, details);
                }

                jsonPayload = jsonObject.toString();
            }

            if (ancHomeVisitActionHelper != null) {
                ancHomeVisitActionHelper.onJsonFormLoaded(jsonPayload, context, details);
                String pre_processed = ancHomeVisitActionHelper.getPreProcessed();
                if (StringUtils.isNotBlank(pre_processed)) {
                    this.jsonPayload = pre_processed;
                }

                String sub_title = ancHomeVisitActionHelper.getPreProcessedSubTitle();
                if (StringUtils.isNotBlank(sub_title)) {
                    this.subTitle = sub_title;
                }

                ScheduleStatus status = ancHomeVisitActionHelper.getPreProcessedStatus();
                if (status != null) {
                    this.scheduleStatus = status;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate that action object has a proper end point destination
     */
    private void validateMe() throws ValidationException {
        if (StringUtils.isBlank(formName) && destinationFragment == null) {
            throw new ValidationException("This action object lacks a valid form or destination fragment");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Status getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Status actionStatus) {
        this.actionStatus = actionStatus;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(String jsonPayload) {
        this.jsonPayload = jsonPayload;
        if (StringUtils.isNotBlank(jsonPayload)) {
            this.setScheduleStatus(ScheduleStatus.DUE);
        }

        // helper processing
        if (ancHomeVisitActionHelper != null) {
            ancHomeVisitActionHelper.onPayloadReceived(jsonPayload);

            String sub_title = ancHomeVisitActionHelper.evaluateSubTitle();
            if (sub_title != null) {
                setSubTitle(sub_title);
            }

            String post_process = ancHomeVisitActionHelper.postProcess(jsonPayload);
            if (post_process != null) {
                this.jsonPayload = ancHomeVisitActionHelper.postProcess(jsonPayload);
            }


            ancHomeVisitActionHelper.onPayloadReceived(this);
        }

        evaluateStatus();
    }


    public void setProcessedJsonPayload(String jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public BaseAncHomeVisitFragment getDestinationFragment() {
        return destinationFragment;
    }

    public void setDestinationFragment(BaseAncHomeVisitFragment destinationFragment) {
        this.destinationFragment = destinationFragment;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public AncHomeVisitActionHelper getAncHomeVisitActionHelper() {
        return ancHomeVisitActionHelper;
    }

    public void setAncHomeVisitActionHelper(AncHomeVisitActionHelper ancHomeVisitActionHelper) {
        this.ancHomeVisitActionHelper = ancHomeVisitActionHelper;
    }

    /**
     * This value will evaluate the json payload as complete if payload is preset
     * or pending if the payload is not present. Any custom execution will also be processed to get the final value
     */
    public void evaluateStatus() {
        setActionStatus(computedStatus());

        if (getAncHomeVisitActionHelper() != null) {
            setActionStatus(getAncHomeVisitActionHelper().evaluateStatusOnPayload());
        }
    }

    public BaseAncHomeVisitAction.Status computedStatus() {
        if (StringUtils.isNotBlank(getJsonPayload())) {
            return Status.COMPLETED;
        } else {
            return Status.PENDING;
        }
    }

    public VaccineWrapper getVaccineWrapper() {
        return (getActionStatus() == Status.COMPLETED) ? vaccineWrapper : null;
    }

    public void setVaccineWrapper(VaccineWrapper vaccineWrapper) {
        this.vaccineWrapper = vaccineWrapper;
    }

    public ServiceWrapper getServiceWrapper() {
        return (getActionStatus() == Status.COMPLETED) ? serviceWrapper : null;
    }

    public void setServiceWrapper(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    public enum Status {COMPLETED, PARTIALLY_COMPLETED, PENDING}

    public enum ScheduleStatus {DUE, OVERDUE}

    public interface AncHomeVisitActionHelper {

        /**
         * Inject values to the json form before rendering
         * Only called once afte the form has been read from the assets folder
         */
        void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details);

        /**
         * executed after form is loaded.
         * Returns a string or null
         */
        String getPreProcessed();

        /**
         * Is executed immediately a json payload is received
         *
         * @param jsonPayload
         */
        void onPayloadReceived(String jsonPayload);


        /**
         * executed after form is loaded on start
         * add functionality to evaluate the state of the view immediately the form is processed
         */
        ScheduleStatus getPreProcessedStatus();

        /**
         * executed after form is loaded on start
         * add functionality to evaluate the subtitle information immediately the form is processed
         */
        String getPreProcessedSubTitle();

        /**
         * add details to process the received payload
         *
         * @param jsonPayload
         */
        String postProcess(String jsonPayload);

        /**
         * executed after the payload is received
         */
        String evaluateSubTitle();

        /**
         * Evaluated after payload is received
         *
         * @return
         */
        Status evaluateStatusOnPayload();

        /**
         * Custom processing after payload is received
         */
        void onPayloadReceived(BaseAncHomeVisitAction ancHomeVisitAction);
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
