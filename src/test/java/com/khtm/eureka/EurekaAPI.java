package com.khtm.eureka;

import com.khtm.eureka.impl.EurekaService;
import com.khtm.eureka.model.HttpResponse;
import com.khtm.eureka.model.Instance;
import com.khtm.eureka.model.Root;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EurekaAPI {
    private static final String TAG = "EurekaAPI";
    private final static String EUREKA_SERVER_URL = "http://10.12.47.125";
    private final static int EUREKA_PORT_NUMBER = 8761;
    private static EurekaService eurekaService = new EurekaService(EUREKA_SERVER_URL, EUREKA_PORT_NUMBER);
    private static List<Instance> instances = new ArrayList<>();

    @BeforeClass
    public static void RegisterServicesOnEurekaServer() throws IOException, JAXBException, InterruptedException, ParserConfigurationException, SAXException {
        System.out.println(String.format("%s - start test %s", TAG, "RegisterServicesOnEurekaServer"));
        HttpResponse responseFirstInstance = eurekaService.registerServiceInEurekaService("foo-application",
                6666, "/health", "/status", "/homepage");
        System.out.println(String.format("[**] HttpResponse -> %s", responseFirstInstance.toString()));
        HttpResponse responseSecondInstance = eurekaService.registerServiceInEurekaService("foo-application",
                8888, "/health", "/status", "/homepage");
        System.out.println(String.format("[**] HttpResponse -> %s", responseSecondInstance.toString()));
        List<Instance> allInstance = eurekaService.getServiceInfo("foo-application");
        allInstance.forEach(instance -> {
            System.out.println(String.format("[*] Service %s - with id %s found", instance.getApp(), instance.getInstanceId()));
            instances.add(instance);
        });
        Assert.assertEquals(responseFirstInstance.getResponseCode(), 204);
        Assert.assertEquals(responseSecondInstance.getResponseCode(), 204);
    }

//    @Test
//    public void ChangeStatus() throws IOException {
//        System.out.println(String.format("%s - start test %s", TAG, "ChangeStatus"));
//        boolean response = eurekaService.changeStatus(instances.get(0).getInstanceId(),
//                "foo-application", EurekaApi.STATUS_DOWN);
//        Assert.assertTrue(response);
//    }

    @Test
    public void getAllServicesInformation() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        System.out.println(String.format("%s - start test %s", TAG, "getAllServicesInformation"));
        Root root = eurekaService.getAllServicesInfo();
        root.getApplications().forEach(application -> {
            application.getInstance().forEach(instance -> {
                System.out.println(String.format("[*] Service %s :::: %s detected.", instance.getApp(), instance.getInstanceId()));
            });
        });
        Assert.assertEquals("1", root.getVersions__delta());
    }

    @Test
    public void getServiceInfo() throws IOException, JAXBException {
        System.out.println(String.format("%s - start test %s", TAG, "getServiceInfo"));
        List<Instance> allInstances = eurekaService.getServiceInfo("foo-application");
        instances = new ArrayList<>();
        allInstances.forEach(instance -> {
            System.out.println(String.format("[*] Service %s with id %s found", instance.getApp(), instance.getInstanceId()));
            instances.add(instance);
        });
        Assert.assertTrue(instances.size() > 1);
    }

    @AfterClass
    public static void unregisterAllServices(){
        System.out.println(String.format("%s - start test %s", TAG, "unregisterAllServices"));
        instances.forEach(instance -> {
            try {
                eurekaService.unregisterServiceFromEurekaService(instance.getApp(), instance.getInstanceId());
                System.out.println(String.format("[*] Service %s -> removed.", instance.getInstanceId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
