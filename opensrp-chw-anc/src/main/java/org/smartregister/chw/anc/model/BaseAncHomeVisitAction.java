package org.smartregister.chw.anc.model;

public class BaseAncHomeVisitAction {

    private String title;
    private String subTitle;
    private Status actionStatus = Status.PENDING;
    private boolean completed = false;

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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public enum Status {COMPLETED, PARTIALLY_COMPLETED, PENDING;}
}
