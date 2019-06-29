package com.khtm.eureka.impl;

import com.khtm.eureka.api.XmlProcessorApi;
import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Applications;
import com.khtm.eureka.model.Instance;
import com.khtm.eureka.model.Root;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.util.List;

public class XmlParse implements XmlProcessorApi {
    @Override
    public Root analysisGetAllServiceInfo(String response) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Root.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Root) unmarshaller.unmarshal(new ByteArrayInputStream(response.getBytes()));
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
