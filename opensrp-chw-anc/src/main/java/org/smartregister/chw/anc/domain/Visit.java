package org.smartregister.chw.anc.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Visit {
    private String visitId;
    private String visitType;
    private String baseEntityId;
    private Date date;
    private String anmId;
    private String locationId;
    private String syncStatus;
    private Date updatedAt;
    private String eventId;
    private String formSubmissionId;
    private String json;
    private Boolean processed;
    private Date createdAt;
    private List<VisitDetail> visitDetails = new ArrayList<>();

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAnmId() {
        return anmId;
    }

    public void setAnmId(String anmId) {
        this.anmId = anmId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFormSubmissionId() {
        return formSubmissionId;
    }

    public void setFormSubmissionId(String formSubmissionId) {
        this.formSubmissionId = formSubmissionId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<VisitDetail> getVisitDetails() {
        return visitDetails;
    }

    public void setVisitDetails(List<VisitDetail> visitDetails) {
        this.visitDetails = visitDetails;
    }
}
