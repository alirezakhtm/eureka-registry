package com.khtm.eureka.impl;

import com.khtm.eureka.api.XmlProcessorApi;
import com.khtm.eureka.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParse implements XmlProcessorApi {
    @Override
    public Root analysisGetAllServiceInfo(String response) throws JAXBException, ParserConfigurationException, IOException, SAXException {
        Root root = new Root();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(response.getBytes()));
        Element rootElement = document.getDocumentElement();
        NodeList nodeListApplications = rootElement.getElementsByTagName("application");
        NodeList nodeListVersionDelta = rootElement.getElementsByTagName("versions__delta");
        NodeList nodeListAppsHashcode = rootElement.getElementsByTagName("apps__hashcode");
        root.setVersions__delta(nodeListVersionDelta.item(0).getNodeValue());
        root.setApps__hashcode(nodeListAppsHashcode.item(0).getNodeValue());
        List<Application> applications = new ArrayList<>();
        for (int i = 0; i < nodeListApplications.getLength(); i++) {
            JAXBContext context = JAXBContext.newInstance(Application.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Application application = (Application) unmarshaller.unmarshal(nodeListApplications.item(i));
            applications.add(application);
        }
        root.setApplication(applications);
        return root;
    }

    @Override
    public Application analysisGetApplicationInfo(String response) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Application.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Application) unmarshaller.unmarshal(new ByteArrayInputStream(response.getBytes()));
    }

    @Override
    public List<Instance> analysisAllSessionsOfService(String response) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Application.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBytes());
        List<Instance> instances = ((Application) unmarshaller.unmarshal(byteArrayInputStream)).getInstance();
        byteArrayInputStream.reset();
        return instances;
    }
}
