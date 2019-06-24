package com.khtm.eureka.model;

import javax.xml.bind.annotation.XmlElement;

public class LeaseInfo {

    private long renewalIntervalInSecs;
    private long durationInSecs;
    private String registrationTimestamp;
    private String lastRenewalTimestamp;
    private String evictionTimestamp;
    private String serviceUpTimestamp;

    @XmlElement(name = "renewalIntervalInSecs")
    public long getRenewalIntervalInSecs() {
        return renewalIntervalInSecs;
    }

    public void setRenewalIntervalInSecs(long renewalIntervalInSecs) {
        this.renewalIntervalInSecs = renewalIntervalInSecs;
    }

    @XmlElement(name = "durationInSecs")
    public long getDurationInSecs() {
        return durationInSecs;
    }

    public void setDurationInSecs(long durationInSecs) {
        this.durationInSecs = durationInSecs;
    }

    @XmlElement(name = "registrationTimestamp")
    public String getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(String registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    @XmlElement(name = "lastRenewalTimestamp")
    public String getLastRenewalTimestamp() {
        return lastRenewalTimestamp;
    }

    public void setLastRenewalTimestamp(String lastRenewalTimestamp) {
        this.lastRenewalTimestamp = lastRenewalTimestamp;
    }

    @XmlElement(name = "evictionTimestamp")
    public String getEvictionTimestamp() {
        return evictionTimestamp;
    }

    public void setEvictionTimestamp(String evictionTimestamp) {
        this.evictionTimestamp = evictionTimestamp;
    }

    @XmlElement(name = "serviceUpTimestamp")
    public String getServiceUpTimestamp() {
        return serviceUpTimestamp;
    }

    public void setServiceUpTimestamp(String serviceUpTimestamp) {
        this.serviceUpTimestamp = serviceUpTimestamp;
    }
}
