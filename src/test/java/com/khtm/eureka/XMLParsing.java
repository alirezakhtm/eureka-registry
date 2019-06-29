package com.khtm.eureka;

import com.khtm.eureka.model.Root;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParsing{

    @Test
    public void parseXmlFormat() throws JAXBException {
        File file = new File(getClass().getClassLoader().getResource("InputSample.xml").getFile());
        JAXBContext context = JAXBContext.newInstance(Root.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Root root = (Root) unmarshaller.unmarshal(file);
        System.out.println(String.format("Number of Services is: %d", root.getApplication().getInstance().size()));
        Assert.assertNotNull(root);
    }

}
