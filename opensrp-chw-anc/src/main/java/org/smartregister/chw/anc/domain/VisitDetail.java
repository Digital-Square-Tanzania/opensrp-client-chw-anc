package org.smartregister.chw.anc.domain;

import java.util.Date;

public class VisitDetail {
    private String visitDetailsId;
    private String visitId;
    private String visitKey;
    private String details; //
    private String jsonDetails;
    private Boolean processed;
    private Date updatedAt;
    private Date createdAt;

    public String getVisitDetailsId() {
        return visitDetailsId;
    }

    public void setVisitDetailsId(String visitDetailsId) {
        this.visitDetailsId = visitDetailsId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getVisitKey() {
        return visitKey;
    }

    public void setVisitKey(String visitKey) {
        this.visitKey = visitKey;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getJsonDetails() {
        return jsonDetails;
    }

    public void setJsonDetails(String jsonDetails) {
        this.jsonDetails = jsonDetails;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
