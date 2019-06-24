package com.khtm.eureka.impl;

import com.khtm.eureka.api.EurekaApi;

public class EurekaService implements EurekaApi {
    public void registerServiceInEurekaService(String applicationName, int portNumber, String hostName,
                                               String healthCheckUrl, String statusPageUrl, String homePageUrl) {

    }

    public boolean changeStatus(String instanceId, String applicationName, String status) {
        return false;
    }

    public String getServiceInfo(String applicationName) {
        return null;
    }

    public String getAllServicesInfo() {
        return null;
    }
}
