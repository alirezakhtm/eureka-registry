package com.khtm.eureka.impl;

import com.khtm.eureka.api.EurekaApi;
import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Root;
import com.sun.istack.internal.NotNull;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.Random;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class EurekaService implements EurekaApi {

    private final String eurekaUrl;
    private final int eurekaPortNum;

    public EurekaService(String eurekaUrl, int eurekaPortNum) {
        this.eurekaPortNum = eurekaPortNum;
        this.eurekaUrl = eurekaUrl;
    }


    public void registerServiceInEurekaService(String applicationName, int portNumber, String hostName,
                                               String healthCheckUrl, String statusPageUrl, String homePageUrl) throws SocketException {
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

/*
        String requestBody = String.format(
                requestThemplate,
                *//*Instance ID*//*
                String.format("%s:%s:%d", this.createCrazyString(20), applicationName, portNumber),
                *//*hostName*//*
                this.getCurrentIP(),
                *//*App Name*//*
                applicationName.toUpperCase(),
                *//*VIP Address*//*
                applicationName.toLowerCase(),
                *//*Secure VIP Address*//*
                applicationName.toLowerCase(),

                );
*/
    }

    public boolean changeStatus(String instanceId, String applicationName, String status) throws IOException {
        String url = String.format("http://%s:%d/eureka/apps/%s/%s/status?value=%s",
                this.eurekaUrl, this.eurekaPortNum, applicationName, instanceId, status);
        return this.sendPutRequest(url, null).getResponseCode() == 200;
    }

    public Application getServiceInfo(String applicationName) throws IOException{
        String url = String.format("http://%s:%d/eureka/apps/%s", this.eurekaUrl, applicationName);
        com.khtm.eureka.model.HttpResponse httpResponse = this.sendGetRequest(url, null);
        XmlParse xmlParse = new XmlParse();
        return xmlParse.analysisGetApplicationInfo(httpResponse.getResult());
    }

    public Root getAllServicesInfo() throws IOException {
        String url = String.format("http://%s:%d/eureka/apps/", this.eurekaUrl, this.eurekaPortNum);
        com.khtm.eureka.model.HttpResponse httpResponse = this.sendGetRequest(url, null);
        XmlParse xmlParse = new XmlParse();
        return xmlParse.analysisGetAllServiceInfo(httpResponse.getResult());
    }

    private com.khtm.eureka.model.HttpResponse sendGetRequest(
            String url, Map<String, String> parameters) throws IOException {
        HttpGet request = null;
        CloseableHttpClient client = null;
        if(parameters != null) {
            // create url request
            String strRequest = addRequestParameterToUrl(url, parameters);
            // create http client
            client = HttpClientBuilder.create().build();
            request = new HttpGet(strRequest);
        }else {
            request = new HttpGet(url);
        }
        request.addHeader("User-Agent", USER_AGENT);
        // receive http response
        assert client != null;
        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while((line = bfr.readLine()) != null) sb.append(line);
        // close client
        client.close();
        // return result to
        return com.khtm.eureka.model.HttpResponse.builder().responseCode(responseCode).result(sb.toString()).build();
    }

    private com.khtm.eureka.model.HttpResponse sendPutRequest(
            String url, Map<String, String> parameters) throws IOException {
        CloseableHttpClient client = null;
        HttpPut request = null;
        String strRequest = "";
        if(parameters != null){
            strRequest = this.addRequestParameterToUrl(url, parameters);
        }else {
            strRequest = url;
        }
        client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        int responceCode = response.getStatusLine().getStatusCode();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = bfr.readLine()) != null){
            sb.append(line);
        }
        client.close();
        return com.khtm.eureka.model.HttpResponse.builder().responseCode(responceCode).result(sb.toString()).build();
    }

    private String addRequestParameterToUrl(String url, @NotNull Map<String, String> parameters){
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
        String IP = null;
        try(DatagramSocket socket = new DatagramSocket()){
            IP = socket.getLocalAddress().getHostAddress();
        }
        return IP;
    }
}
