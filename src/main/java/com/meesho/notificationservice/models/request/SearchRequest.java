package com.meesho.notificationservice.models.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRequest {

    private  String message;
    private String phoneNumber;

    private long startCreatedAt;
    private long endCreatedAt;


}
