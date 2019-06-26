package org.smartregister.chw.anc.domain;

import java.util.Date;

public class VisitDetail {
    private String visitDetailsId;
    private String visitId;
    private String serviceGroup; // vaccine , recurring , contact , etc
    private String serviceType; // IPTp-sp
    private String service; // IPTp-sp1
    private String externalVisitID; // reference to the external table / service if the service is created in a separate table
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

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getExternalVisitID() {
        return externalVisitID;
    }

    public void setExternalVisitID(String externalVisitID) {
        this.externalVisitID = externalVisitID;
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
