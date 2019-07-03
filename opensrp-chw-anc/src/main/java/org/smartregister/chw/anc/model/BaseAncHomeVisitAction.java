package org.smartregister.chw.anc.model;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;

/**
 * This action list allows users to either load a form or link it to a separate fragment.
 */
public class BaseAncHomeVisitAction {

    private String title;
    private String subTitle;
    private Status actionStatus = Status.PENDING;
    private ScheduleStatus scheduleStatus = ScheduleStatus.DUE;
    private boolean optional;
    private BaseAncHomeVisitFragment destinationFragment;
    private String formName;
    private String jsonPayload;
    private String selectedOption;
    private AncHomeVisitActionHelper ancHomeVisitActionHelper;
    private VaccineWrapper vaccineWrapper;
    private ServiceWrapper serviceWrapper;

    // event based behaviors
    private Runnable onPayLoadReceived;

    public BaseAncHomeVisitAction(String title, String subTitle, boolean optional, BaseAncHomeVisitFragment destinationFragment, String formName) throws ValidationException {
        this.title = title;
        this.subTitle = subTitle;
        this.optional = optional;
        this.destinationFragment = destinationFragment;
        this.formName = formName;

        validateMe();
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
        evaluateStatus();
        if (onPayLoadReceived != null) {
            onPayLoadReceived.run();
        }
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

    public void setOnPayLoadReceived(Runnable onPayLoadReceived) {
        this.onPayLoadReceived = onPayLoadReceived;
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
        Status evaluateStatusOnPayload();
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
