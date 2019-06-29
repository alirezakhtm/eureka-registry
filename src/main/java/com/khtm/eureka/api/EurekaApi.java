package com.khtm.eureka.api;

import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.HttpResponse;
import com.khtm.eureka.model.Instance;
import com.khtm.eureka.model.Root;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface EurekaApi {

    static String STATUS_UP = "UP";
    static String STATUS_DOWN = "DOWN";

    HttpResponse registerServiceInEurekaService(
            String applicationName,
            int portNumber,
            String healthCheckUrl,
            String statusPageUrl,
            String homePageUrl
    ) throws IOException;

    HttpResponse unregisterServiceFromEurekaService(String applicationName, String instanceId) throws IOException;

    boolean changeStatus(String instanceId, String applicationName, String status) throws IOException;

    List<Instance> getServiceInfo(String applicationName) throws IOException, JAXBException;

    Root getAllServicesInfo() throws IOException, JAXBException;

}
