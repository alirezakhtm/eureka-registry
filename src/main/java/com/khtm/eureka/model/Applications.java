package com.khtm.eureka.model;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Applications {

    private List<Application> applications;

    @XmlElement(name = "application")
    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
