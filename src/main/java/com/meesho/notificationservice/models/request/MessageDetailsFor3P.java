package com.meesho.notificationservice.models.request;

import lombok.*;

import javax.persistence.Table;
import javax.print.attribute.standard.Destination;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Builder
@Data
public class MessageDetailsFor3P {

    private String deliverychannel;
    private Channels channels;
    private List<Destination> destination;

    @Getter @Setter  @ToString @Builder
    public static class Channels{
        private Sms sms;


    }
    @Getter @Setter @ToString @Builder
    public static class  Sms{
        private String text;
    }
    @Getter @Setter @ToString @Builder
    public  static class Destination{
        private List<String> msisdn;
        private String correlationId;
    }
}
