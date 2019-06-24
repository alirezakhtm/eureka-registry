package com.khtm.eureka.api;

public interface EurekaApi {

    public static String STATUS_UP = "UP";
    public static String STATUS_DOWN = "DOWN";

    void registerServiceInEurekaService(
            String applicationName,
            int portNumber,
            String hostName,
            String healthCheckUrl,
            String statusPageUrl,
            String homePageUrl
    );

    boolean changeStatus(String instanceId, String applicationName, String status);

    String getServiceInfo(String applicationName);

    String getAllServicesInfo();

}
