package com.khtm.example.springexample;

import com.khtm.eureka.impl.EurekaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

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

    @Bean
    public void registerApplication() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        EurekaService eurekaService = new EurekaService(eurekaServerUrl, Integer.parseInt(eurekaServerPort));
        eurekaService.registerServiceInEurekaService(
                applicationName,
                applicationPortNum,
                healthCheckURL,
                statusPageURL,
                homePageURL
        );
    }

}
