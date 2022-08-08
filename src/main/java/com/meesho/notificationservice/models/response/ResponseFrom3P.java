package com.meesho.notificationservice.models.response;

import lombok.Data;
import lombok.ToString;

import javax.xml.ws.Response;
import java.util.ArrayList;

@Data
@ToString
public class ResponseFrom3P {

    private ArrayList<Response> response;

    @Data
    public static class Response{
        private String code;
        private String transiD;
        private String description;
        private String correlationid;
    }
}
