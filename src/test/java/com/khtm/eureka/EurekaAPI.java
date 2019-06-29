package com.khtm.eureka;

import com.khtm.eureka.api.EurekaApi;
import com.khtm.eureka.impl.EurekaService;
import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.HttpResponse;
import com.khtm.eureka.model.Root;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public class EurekaAPI {

    private final static String EUREKA_SERVER_URL = "10.12.47.125";
    private final static int EUREKA_PORT_NUMBER = 8761;
    private static EurekaService eurekaService = new EurekaService(EUREKA_SERVER_URL, EUREKA_PORT_NUMBER);
    private static List<Application> applications;

    @BeforeClass
    public static void RegisterServicesOnEurekaServer() throws IOException, JAXBException {
        HttpResponse responseFirstInstance = eurekaService.registerServiceInEurekaService("foo-application",
                5555, "/health", "/status", "/homepage");
        HttpResponse responseSecondInstance = eurekaService.registerServiceInEurekaService("foo-application",
                9999, "/health", "/status", "/homepage");
        applications = eurekaService.getServiceInfo("foo-application");
        Assert.assertEquals(Math.round(responseFirstInstance.getResponseCode()/100), 2);
        Assert.assertEquals(Math.round(responseSecondInstance.getResponseCode()/100), 2);
    }

    @Test
    public void ChangeStatus() throws IOException {
        boolean response = eurekaService.changeStatus(applications.get(0).getInstance().getInstanceId(),
                "foo-application", EurekaApi.STATUS_DOWN);
        Assert.assertTrue(response);
    }

    @Test
    public void getAllServicesInformation() throws IOException, JAXBException {
        Root root = eurekaService.getAllServicesInfo();
        Assert.assertEquals("1", root.getVersions__delta());
    }

    @Test
    public void getServiceInfo() throws IOException, JAXBException {
        List<Application> applications = eurekaService.getServiceInfo("foo-application");
        Assert.assertTrue(applications.size() > 0);
    }

    @AfterClass
    public static void unregisterAllServices(){
        // TODO
    }
}
