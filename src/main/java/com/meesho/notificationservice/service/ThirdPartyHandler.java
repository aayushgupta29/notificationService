package com.meesho.notificationservice.service;

import com.meesho.notificationservice.models.SearchEntity;
import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.models.request.MessageDetailsFor3P;
import com.meesho.notificationservice.models.response.ResponseFrom3P;
import com.meesho.notificationservice.repository.SmsRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

import static com.meesho.notificationservice.constants.Constants.KEY;
import static com.meesho.notificationservice.constants.Constants.THIRD_PARTY_URL;

@Service
public class ThirdPartyHandler {

    private static final Logger LOGGER  = LoggerFactory.getLogger(ThirdPartyHandler.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsRequestRepository smsRequestRepository;
    @Autowired
    private ElasticSearchService elasticSearchService;

    public void sendThirdPartySms(SmsRequest smsRequest){

        ResponseFrom3P.Response response = new ResponseFrom3P.Response();
        try{
            MessageDetailsFor3P messageDetailsFor3P = prepareMessageDetails(smsRequest);
            ResponseFrom3P responseFrom3P = sendMessageVia3P(messageDetailsFor3P);
            response = responseFrom3P.getResponse().get(0);
        }
        catch (Exception e){
            e.printStackTrace();
            smsRequest.setUpdatedAt(System.currentTimeMillis());
            smsRequest.setFailureComments("Either third party key is not right or any other parameter is wrong");
            smsRequest.setStatus("FAILED");
            smsRequest.setFailureCode("ERR_THIRDPARTYTIMEOUT");
            smsRequestRepository.save(smsRequest);
            LOGGER.error(String.format("Message not sent fun error"));
            return;
        }
        if(response.getCode().equals("1001")){
            addToElasticSearch(smsRequest);
            smsRequest.setUpdatedAt(System.currentTimeMillis());
            smsRequest.setStatus("SUCCESS");
            smsRequestRepository.save(smsRequest);
            LOGGER.info("in success");
        }
        else{
            smsRequest.setUpdatedAt(System.currentTimeMillis());
            smsRequest.setFailureComments("Message not sent successful third party API give error");
            smsRequest.setStatus("FAILURE");
            smsRequest.setFailureCode("ERR_EXTERNAL");
            smsRequestRepository.save(smsRequest);
            LOGGER.error(String.format("Message not sent successful third party API give error"));
        }
    }

    private void addToElasticSearch(SmsRequest smsRequest){
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setId(smsRequest.getId());
        searchEntity.setPhoneNumber(smsRequest.getPhoneNumber());
        searchEntity.setMessage(smsRequest.getMessage());
        searchEntity.setCreatedAt(smsRequest.getCreatedAt());
        elasticSearchService.save(searchEntity);
    }

    private MessageDetailsFor3P prepareMessageDetails(SmsRequest smsRequest){
        return MessageDetailsFor3P.builder()
            .deliverychannel("sms")
            .channels(MessageDetailsFor3P.Channels.builder()
                .sms(MessageDetailsFor3P.Sms.builder().text(smsRequest.getMessage()).build()).build())
            .destination(
                Collections.singletonList(
                    MessageDetailsFor3P.Destination.builder()
                       .correlationId(String.valueOf(smsRequest.getId()))
                       .msisdn(Collections.singletonList(smsRequest.getPhoneNumber()))
                       .build()))
            .build();
    }

    private HttpEntity<MessageDetailsFor3P> prepareHttpEntity(MessageDetailsFor3P messageDetailsFor3P){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("key", KEY);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return  new HttpEntity<>(messageDetailsFor3P, headers);
    }

    private ResponseFrom3P sendMessageVia3P(MessageDetailsFor3P messageDetailsFor3P){

        HttpEntity<MessageDetailsFor3P> entity = prepareHttpEntity(messageDetailsFor3P);
        try{
            return restTemplate.postForObject(
                    THIRD_PARTY_URL, entity, ResponseFrom3P.class
            );
        }
        catch (Exception e){
            e.printStackTrace();
            return  new ResponseFrom3P();
        }
    }
}
