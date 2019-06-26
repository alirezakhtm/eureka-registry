package com.khtm.eureka.impl;

import com.khtm.eureka.api.XmlProcessorApi;
import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Root;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlParse implements XmlProcessorApi {
    @Override
    public Root analysisGetAllServiceInfo(String response) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Root.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Root root = (Root) unmarshaller.unmarshal(response);
        return null;
    }

    @Override
    public Application analysisGetApplicationInfo(String response) {
        return null;
    }
}
