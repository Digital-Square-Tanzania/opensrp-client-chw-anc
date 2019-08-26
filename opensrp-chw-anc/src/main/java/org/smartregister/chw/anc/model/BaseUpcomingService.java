package org.smartregister.chw.anc.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseUpcomingService {

    private List<String> serviceName = new ArrayList<>();
    private Date serviceDate;
    private List<BaseUpcomingService> upcomingServiceList;

    public BaseUpcomingService() {
    }

    public BaseUpcomingService(String serviceName) {
        this.serviceName.add(serviceName);
    }

    public String getServiceName() {
        return (serviceName.size() > 0) ? serviceName.get(0) : "" ;
    }

    public void setServiceName(String serviceName) {
        this.serviceName.add(serviceName);
    }
    public List<String> getServiceNames() {
        return serviceName ;
    }

    public void setServiceName(@NonNull List<String> serviceNames) {
        this.serviceName.addAll(serviceNames);
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
