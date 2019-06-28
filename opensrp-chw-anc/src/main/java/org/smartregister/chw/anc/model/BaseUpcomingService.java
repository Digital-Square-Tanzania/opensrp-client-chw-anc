package org.smartregister.chw.anc.model;

import java.util.Date;
import java.util.List;

public class BaseUpcomingService {

    private String serviceName;
    private Date serviceDate;
    private List<BaseUpcomingService> upcomingServiceList;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public List<BaseUpcomingService> getUpcomingServiceList() {
        return upcomingServiceList;
    }

    public void setUpcomingServiceList(List<BaseUpcomingService> upcomingServiceList) {
        this.upcomingServiceList = upcomingServiceList;
    }
}
