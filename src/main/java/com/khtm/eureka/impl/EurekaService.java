package com.khtm.eureka.impl;

import com.google.gson.GsonBuilder;
import com.khtm.eureka.api.EurekaApi;
import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Instance;
import com.khtm.eureka.model.Root;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * @author a.r.khatamidoost
 * */
public class EurekaService implements EurekaApi {

    private final String eurekaUrl;
    private final int eurekaPortNum;
    private final String ipv4;

    private static String instanceId;
    private static String applicationName;
    private static String healthCheckUrl;
    private static int applicationPortNumber;

    private static Thread threadHealthCheck;

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String REQUEST_METHOD_PUT = "PUT";
    private static final String REQUEST_METHOD_DELETE = "DELETE";

    private static List<Instance> instancesDeactivated = new ArrayList<>();

    /**
     * @param eurekaUrl like http://10.12.47.125
     * @param eurekaPortNum like 8761
     * */
    public EurekaService(String eurekaUrl, int eurekaPortNum, String ipv4) {
        this.eurekaPortNum = eurekaPortNum;
        this.eurekaUrl = eurekaUrl;
        this.ipv4 = ipv4;
        threadHealthCheck = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    //System.out.println(">>>> Thread is running ...");
                    String url = String.format("http://%s:%d%s", getCurrentIP(), applicationPortNumber, healthCheckUrl);
                    com.khtm.eureka.model.HttpResponse httpResponse = sendRequest(url,
                            null, REQUEST_METHOD_GET);
                    if (httpResponse.getResponseCode() != 200) {
                        unregisterThisService();
                    }
                } catch (IOException e) {
                    try {
                        unregisterThisService();
                        break;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        /*EurekaService.threadHealthCheck.setDaemon(true);
        EurekaService.threadHealthCheck.start();*/
        Runtime.getRuntime().addShutdownHook(threadHealthCheck);
    }

    /**
     * @param applicationName like foo-app
     * @param portNumber port number that application uses it.
     * @param healthCheckUrl like /app/health
     * @param statusPageUrl like /app/status
     * @param homePageUrl like /app/home
     * @return response code and content
     * */
    public com.khtm.eureka.model.HttpResponse registerServiceInEurekaService(String applicationName, int portNumber,
                                                                             String healthCheckUrl, String statusPageUrl,
                                                                             String homePageUrl) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        String requestThemplate = "{\n" +
                "\t\"instance\": {\n" +
                "\t\t\"instanceId\": \"%s\",\n" +
                "\t\t\"hostName\": \"%s\",\n" +
                "\t\t\"app\": \"%s\",\n" +
                "\t\t\"vipAddress\": \"%s\",\n" +
                "\t\t\"secureVipAddress\": \"%s\",\n" +
                "\t\t\"ipAddr\": \"%s\",\n" +
                "\t\t\"status\": \"%s\",\n" +
                "\t\t\"port\": {\"$\": \"%d\", \"@enabled\": \"true\"},\n" +
                "\t\t\"securePort\": {\"$\": \"443\", \"@enabled\": \"false\"},\n" +
                "\t\t\"healthCheckUrl\": \"%s\",\n" +
                "\t\t\"statusPageUrl\": \"%s\",\n" +
                "\t\t\"homePageUrl\": \"%s\",\n" +
                "\t\t\"dataCenterInfo\": {\n" +
                "\t\t\t\"@class\": \"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\", \n" +
                "\t\t\t\"name\": \"MyOwn\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        instanceId = String.format("%s:%s:%d", this.createCrazyString(10), applicationName, portNumber);
        EurekaService.applicationName = applicationName;
        EurekaService.healthCheckUrl = healthCheckUrl;
        EurekaService.applicationPortNumber = portNumber;

        String requestBody = String.format(
                requestThemplate,
                /*Instance ID*/
                instanceId,
                /*hostName*/
                this.getCurrentIP(),
                /*App Name*/
                applicationName.toUpperCase(),
                /*VIP Address*/
                applicationName.toLowerCase(),
                /*Secure VIP Address*/
                applicationName.toLowerCase(),
                /*IP Address*/
                this.getCurrentIP(),
                /*status*/
                "UP",
                /*Port*/
                portNumber,
                /*Health Check URL*/
                String.format("http://%s:%d%s", this.getCurrentIP(), portNumber, healthCheckUrl),
                /*Status Page URL*/
                String.format("http://%s:%d%s", this.getCurrentIP(), portNumber, statusPageUrl),
                /*Home Page URL*/
                String.format("http://%s:%d%s", this.getCurrentIP(), portNumber, homePageUrl)
                );

        String url = String.format("%s:%d/eureka/apps/%s",
                this.eurekaUrl, this.eurekaPortNum, applicationName.toUpperCase());

        StringEntity requestEntity = new StringEntity(requestBody, "application/json", "UTF-8");
        HttpPost request = new HttpPost(url);
        request.setEntity(requestEntity);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        int responseCodeNum = response.getStatusLine().getStatusCode();
        String line = "";
        StringBuilder sb = new StringBuilder();
        if(response.getEntity() != null){
            BufferedReader bfr = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            client.close();
            while ((line = bfr.readLine()) != null) sb.append(line);
            //this.healthCheckOtherServices(instanceId);
            return com.khtm.eureka.model.HttpResponse.builder().responseCode(responseCodeNum).result(sb.toString()).build();
        }else{
            client.close();
            //this.healthCheckOtherServices(instanceId);
            return com.khtm.eureka.model.HttpResponse.builder().responseCode(responseCodeNum).result(null).build();
        }
    }

    @Override
    public com.khtm.eureka.model.HttpResponse unregisterServiceFromEurekaService(String applicationName, String instanceId) throws IOException {
        String url = String.format("%s:%d/eureka/apps/%s/%s", this.eurekaUrl, this.eurekaPortNum, applicationName, instanceId);
        return this.sendRequest(url, null, EurekaService.REQUEST_METHOD_DELETE);
    }

    /**
     * @param instanceId specific ID for any services that registered on Eureka server
     * @param applicationName application name that defined in properties file
     * @param status current status service like up, down
     * @return true if response code is equal to 200
     * */
    public boolean changeStatus(String instanceId, String applicationName, String status) throws IOException {
        String url = String.format("%s:%d/eureka/apps/%s/%s/status?value=%s",
                this.eurekaUrl, this.eurekaPortNum, applicationName.toUpperCase(), instanceId, status);
        return this.sendRequest(url, null, EurekaService.REQUEST_METHOD_PUT).getResponseCode() == 200;
    }

    /**
     * @param applicationName application name that defined in properties file
     * @return all information about application
     * */
    public List<Instance> getServiceInfo(String applicationName) throws IOException, JAXBException {
        String url = String.format("%s:%d/eureka/apps/%s", this.eurekaUrl, this.eurekaPortNum,applicationName);
        com.khtm.eureka.model.HttpResponse httpResponse = this.sendRequest(url, null, EurekaService.REQUEST_METHOD_GET);//this.sendGetRequest(url, null);
        XmlParse xmlParse = new XmlParse();
        return xmlParse.analysisAllSessionsOfService(httpResponse.getResult());
    }

    /**
     * @return all information about all services which registered in Eureka server.
     * */
    public Root getAllServicesInfo() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        String url = String.format("%s:%d/eureka/apps/", this.eurekaUrl, this.eurekaPortNum);
        com.khtm.eureka.model.HttpResponse httpResponse = this.sendRequest(url, null, EurekaService.REQUEST_METHOD_GET);//this.sendGetRequest(url, null);
        XmlParse xmlParse = new XmlParse();
        return xmlParse.analysisGetAllServiceInfo(httpResponse.getResult());
    }

    @Override
    public void unregisterThisService() throws IOException {
        this.unregisterServiceFromEurekaService(EurekaService.applicationName, instanceId);
    }

    private String addRequestParameterToUrl(String url, Map<String, String> parameters){
        String strRequest = url + "?";
        for(String key : parameters.keySet()){
            strRequest += String.format("%s=%s&", key, parameters.get(key));
        }
        if(strRequest.endsWith("&")) strRequest = strRequest.substring(0, strRequest.lastIndexOf("&"));
        return strRequest;
    }

    private String createCrazyString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz123456789";
        String[] strArray = str.split("|");
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(strArray[random.nextInt(strArray.length-1)] + "");
        }
        return sb.toString();
    }

    private String getCurrentIP() throws SocketException {
        /*String IP = null;
        try(DatagramSocket socket = new DatagramSocket()){
            IP = socket.getLocalAddress().getHostAddress();
        }
        return IP;*/
        return this.ipv4;
    }

    private com.khtm.eureka.model.HttpResponse sendRequest(
            String url, Map<String, String> parameters, String requestMethod) throws IOException {
        HttpRequestBase request = null;
        // create http client
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String strRequest = null;
        if(parameters != null) {
            // create url request
            strRequest = addRequestParameterToUrl(url, parameters);
        }else {
            strRequest = url;
        }
        switch (requestMethod){
            case REQUEST_METHOD_GET:
                request = new HttpGet(strRequest);
                break;
            case REQUEST_METHOD_PUT:
                request = new HttpPut(strRequest);
                break;
            case REQUEST_METHOD_DELETE:
                request = new HttpDelete(strRequest);
                break;
        }
        request.addHeader("User-Agent", USER_AGENT);
        // receive http response
        assert client != null;
        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();
        if(response.getEntity() != null) {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = bfr.readLine()) != null) sb.append(line);
            // close client
            client.close();
            // return result to
            return com.khtm.eureka.model.HttpResponse.builder().responseCode(responseCode).result(sb.toString()).build();
        }else{
            // close client
            client.close();
            // return result to
            return com.khtm.eureka.model.HttpResponse.builder().responseCode(responseCode).result(null).build();
        }
    }

    private void healthCheckOtherServices(String currentInstanceId) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        Root root = this.getAllServicesInfo();
        List<Application> applications = root.getApplications();

        applications.forEach(application -> {
            List<Instance> instances = application.getInstance();
            instances.forEach(instance -> {
                String healthCheckUrl = instance.getHealthCheckUrl();
                try {
                    com.khtm.eureka.model.HttpResponse response = this.sendRequest(
                            healthCheckUrl, null, EurekaService.REQUEST_METHOD_GET);
                    Status healthStatus = new GsonBuilder().create().fromJson(response.getResult(), Status.class);
                    if(!healthStatus.getStatus().toUpperCase().equals(EurekaApi.STATUS_UP)){
                        instancesDeactivated.add(instance);
                    }

                } catch (IOException e) {
                    instancesDeactivated.add(instance);
                    System.out.println(String.format("Service added to deactivated services -> %s", instance.getInstanceId()));
                }
            });
            instancesDeactivated.forEach(instance -> {
                try {
                    if(!instance.getInstanceId().equals(currentInstanceId)){
                        this.unregisterServiceFromEurekaService(instance.getApp().toUpperCase(), instance.getInstanceId());
                        System.out.println(String.format("[INSTANCE ID REMOVED] :::: %S", instance.getInstanceId()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    @Data
    private class Status {
        private String status;
    }
}
