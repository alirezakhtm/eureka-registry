package com.khtm.eureka.api;

import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.HttpResponse;
import com.khtm.eureka.model.Root;

import java.io.IOException;

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

    boolean changeStatus(String instanceId, String applicationName, String status) throws IOException;

    Application getServiceInfo(String applicationName) throws IOException;

    Root getAllServicesInfo() throws IOException;

}
