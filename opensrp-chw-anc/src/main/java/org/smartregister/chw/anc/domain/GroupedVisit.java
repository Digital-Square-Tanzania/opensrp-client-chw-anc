package org.smartregister.chw.anc.domain;

import java.util.List;

public class GroupedVisit {
    private String baseEntityId;
    private String name;
    private List<Visit> visitList;

    public GroupedVisit(String baseEntityId, String name, List<Visit> visitList) {
        this.baseEntityId = baseEntityId;
        this.name = name;
        this.visitList = visitList;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Visit> getVisitList() {
        return visitList;
    }

    public void setVisitList(List<Visit> visitList) {
        this.visitList = visitList;
    }
}
