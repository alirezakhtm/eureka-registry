# Eureka Registry Lib
This library designed for using of Eureka service discovery without depending to spring framework.
even though this library can used in spring framework but it is not depend to any framework. Eureka server has some HTTP
API that all microservices use them to communicate, All these APIs implemented in this project.

## Initial parameters
* **Application Name**: name of application that it will be detected by Eureka server.
* **Eureka Server URL**: eureka server URL must be defined here, for example ``http://10.12.47.125``
* **Eureka Server Port**: port number that eureka server use it, for example ``8761``
* **Application IP**: IP of server or container that application (microservice) has been run on it, for example ``10.12.46.147``
* **Health Check URL**: response of this URL must be like ``{"status":"UP"}``
* **Status URL**
* **Home Page URL**

This project contains example how to use this library in your project.
```java
@Configuration
public class ApplicationConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    int applicationPortNum;

    @Value("${com.khtm.eureka.server.url}")
    String eurekaServerUrl;

    @Value("${com.khtm.eureka.server.port}")
    String eurekaServerPort;

    @Value("${com.khtm.application.healthcheckurl}")
    String healthCheckURL;

    @Value("${com.khtm.application.statuspageurl}")
    String statusPageURL;

    @Value("${com.khtm.application.homepageurl}")
    String homePageURL;

    @Value("${com.khtm.application.ipv4}")
    String ipv4Address;

    @Bean
    public EurekaService getEurekaService(){
        return new EurekaService(eurekaServerUrl, Integer.parseInt(eurekaServerPort), ipv4Address);
    }

    @Bean
    public void registerApplication() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        EurekaService eurekaService = getEurekaService();
        eurekaService.registerServiceInEurekaService(
                applicationName,
                applicationPortNum,
                healthCheckURL,
                statusPageURL,
                homePageURL
        );
    }
}
``` 

# Vision
In next version I would like to develop this project and provide it for OSGi framework. 
