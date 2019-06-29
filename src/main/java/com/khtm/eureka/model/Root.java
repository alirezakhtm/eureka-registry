package com.khtm.eureka.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "applications")
public class Root {

    private String versions__delta;
    private String apps__hashcode;
    private Application application;

    @XmlElement(name = "versions__delta")
    public String getVersions__delta() {
        return versions__delta;
    }

    public void setVersions__delta(String versions__delta) {
        this.versions__delta = versions__delta;
    }

    @XmlElement(name = "apps__hashcode")
    public String getApps__hashcode() {
        return apps__hashcode;
    }

    public void setApps__hashcode(String apps__hashcode) {
        this.apps__hashcode = apps__hashcode;
    }

    @XmlElement(name = "application")
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application applications) {
        this.application = applications;
    }
}
