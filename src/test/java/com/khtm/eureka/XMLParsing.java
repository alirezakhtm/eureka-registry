package com.khtm.eureka;

import com.khtm.eureka.model.Application;
import org.junit.Assert;
import org.junit.Test;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParsing{

    @Test
    public void parseXmlFormat() throws JAXBException, ParserConfigurationException, IOException, SAXException {
        /*File file = new File(getClass().getClassLoader().getResource("InputSample.xml").getFile());
        JAXBContext context = JAXBContext.newInstance(Root.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Root root = (Root) unmarshaller.unmarshal(file);
        //System.out.println(String.format("Number of Services is: %d", root.getApplications().size()));
        Assert.assertNotNull(root);*/

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(getClass().getClassLoader().getResource("InputSample.xml").getFile()));
        Element rootElement = document.getDocumentElement();
        NodeList nodeList = rootElement.getElementsByTagName("application");
        List<Application> applications = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            JAXBContext context = JAXBContext.newInstance(Application.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Application application = (Application) unmarshaller.unmarshal(nodeList.item(i));
            applications.add(application);
        }
        applications.forEach(application -> {
            System.out.println(application);
        });
        Assert.assertEquals(applications.size(), 7);
    }

}
