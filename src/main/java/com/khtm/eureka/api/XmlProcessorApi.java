package com.khtm.eureka.api;

import com.khtm.eureka.model.Application;
import com.khtm.eureka.model.Root;

import java.util.List;

public interface XmlProcessorApi {

    Root analysisGetAllServiceInfo(String response);
    Application analysisGetApplicationInfo(String response);

}
