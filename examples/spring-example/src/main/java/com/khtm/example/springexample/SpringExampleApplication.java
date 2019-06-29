package com.khtm.example.springexample;

import com.khtm.eureka.impl.EurekaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.IOException;

@SpringBootApplication
public class SpringExampleApplication {


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

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringExampleApplication.class, args);
    }

    @Bean
    public void registerApplication() throws IOException {
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
