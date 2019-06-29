package com.khtm.eureka.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class HttpResponse {

    private int responseCode;
    private String result;

}
