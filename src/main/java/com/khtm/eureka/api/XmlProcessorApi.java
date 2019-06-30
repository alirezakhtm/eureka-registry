package com.khtm.eureka.api;

import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Instance;
import com.khtm.eureka.model.Root;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface XmlProcessorApi {

    Root analysisGetAllServiceInfo(String response) throws JAXBException, ParserConfigurationException, IOException, SAXException;
    Application analysisGetApplicationInfo(String response) throws JAXBException;
    List<Instance> analysisAllSessionsOfService(String response) throws JAXBException;

}
