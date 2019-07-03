package com.khtm.eureka.example.war;

import com.khtm.eureka.impl.EurekaService;
import org.xml.sax.SAXException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class RegistrationListener implements ServletContextListener {

    private static EurekaService eurekaService = null;

    private static final String applicationName = "example-war";
    private static final String healthCheckURL = "/example-war/health";
    private static final String statusURL = "/example-war/status";
    private static final String homePageURL = "/example-war/";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if(eurekaService == null){
            eurekaService = new EurekaService("http://10.12.47.125", 8761, "10.12.46.147");
            try {
                eurekaService.registerServiceInEurekaService(applicationName, 9090,
                        healthCheckURL, statusURL, homePageURL);
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            eurekaService.unregisterServiceFromEurekaService(applicationName, EurekaService.instanceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
