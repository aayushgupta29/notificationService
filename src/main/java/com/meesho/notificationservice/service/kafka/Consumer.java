package com.meesho.notificationservice.service.kafka;

import com.meesho.notificationservice.models.response.ResponseFrom3P;
import com.meesho.notificationservice.models.SearchEntity;
import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.repository.SmsRequestRepository;

import com.meesho.notificationservice.service.BlacklistService;
import com.meesho.notificationservice.service.ElasticSearchService;
import com.meesho.notificationservice.service.ThirdPartyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;


import static com.meesho.notificationservice.constants.Constants.TOPIC;

@Service
public class Consumer {

    private static final Logger LOGGER  = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private SmsRequestRepository smsRequestRepository;

    @Autowired
    private BlacklistService blacklistService;



    @Autowired
    private  ThirdPartyHandler thirdPartyHandler;


    @KafkaListener(topics=TOPIC, groupId="myGroup")
    public void consumer(int messageId) {


        LOGGER.info(String.format("MESSAGE has been received %s", messageId));

        SmsRequest smsRequest = smsRequestRepository.findById(messageId);

        boolean isPhoneNumberBlacklisted = blacklistService.getStatusOfBlacklistNumber(smsRequest.getPhoneNumber());


        if(isPhoneNumberBlacklisted){
            LOGGER.info(String.format("Phone no is blacklisted"));

            smsRequest.setUpdatedAt(System.currentTimeMillis());
            smsRequest.setFailureComments("Phone Number is blacklisted");
            smsRequest.setStatus("FAILED");
            smsRequest.setFailureCode("ERR_BLACKLIST");
            smsRequestRepository.save(smsRequest);

        }else{

            thirdPartyHandler.sendThirdPartySms(smsRequest);

        }
    }


}
