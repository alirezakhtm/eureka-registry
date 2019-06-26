package com.khtm.eureka;

import com.khtm.eureka.api.EurekaApi;
import com.khtm.eureka.impl.EurekaService;
import com.khtm.eureka.model.HttpResponse;
import com.khtm.eureka.model.Root;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class EurekaAPI {

    private final static String EUREKA_SERVER_URL = "10.12.47.125";
    private final static int EUREKA_PORT_NUMBER = 8761;
    private EurekaService eurekaService = new EurekaService(EUREKA_SERVER_URL, EUREKA_PORT_NUMBER);

    @Test
    public void RegisterServiceOnEurekaServer() throws IOException {
        HttpResponse response = eurekaService.registerServiceInEurekaService("foo-pplication",
                5555, "/health", "/status", "/homepage");
        Assert.assertEquals(Math.round(response.getResponseCode()/100), 2);
    }

    @Test
    public void ChangeStatus() throws IOException {
        boolean response = eurekaService.changeStatus("znbco2mlyfl3i2nz7nbv:foo-pplication:5555",
                "foo-application", EurekaApi.STATUS_UP);
        Assert.assertTrue(response);
    }

    @Test
    public void getAllServicesInformation() throws IOException, JAXBException {
        Root root = eurekaService.getAllServicesInfo();
        Assert.assertEquals("1", root.getVersions__delta());
    }

    @Test
    public void getServiceInfo(){

    }


}
