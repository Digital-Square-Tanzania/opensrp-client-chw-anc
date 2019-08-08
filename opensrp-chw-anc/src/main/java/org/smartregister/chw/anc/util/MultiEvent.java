package org.smartregister.chw.anc.util;

import org.smartregister.clientandeventmodel.Event;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;
import java.util.List;

public class MultiEvent {

    private Event event;
    private List<VaccineWrapper> vaccines = new ArrayList<>();
    private List<ServiceWrapper> services = new ArrayList<>();

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<VaccineWrapper> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<VaccineWrapper> vaccines) {
        this.vaccines = vaccines;
    }

    public List<ServiceWrapper> getServices() {
        return services;
    }

    public void setServices(List<ServiceWrapper> services) {
        this.services = services;
    }
}
