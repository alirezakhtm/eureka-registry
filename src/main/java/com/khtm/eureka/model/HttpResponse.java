package com.khtm.eureka.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HttpResponse {

    private int responseCode;
    private String result;

}
