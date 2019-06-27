package org.smartregister.chw.anc.model;

import java.util.List;

public class BaseHomeVisitHistoricAction {

    private String Title = "";
    private List<String> details = null;

    public BaseHomeVisitHistoricAction(String title, List<String> details) {
        Title = title;
        this.details = details;
    }

    public BaseHomeVisitHistoricAction() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
