package com.khtm.eureka.api;

import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Instance;
import com.khtm.eureka.model.Root;

import javax.xml.bind.JAXBException;
import java.util.List;

public interface XmlProcessorApi {

    Root analysisGetAllServiceInfo(String response) throws JAXBException;
    Application analysisGetApplicationInfo(String response) throws JAXBException;
    List<Instance> analysisAllSessionsOfService(String response) throws JAXBException;

}
