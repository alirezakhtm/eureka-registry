package com.khtm.eureka.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Metadata {

    private String className;

    @XmlAttribute(name = "class")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
