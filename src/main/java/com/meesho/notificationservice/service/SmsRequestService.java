package com.meesho.notificationservice.service;

import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.models.response.SuccessResponseEntity;
import com.meesho.notificationservice.repository.SmsRequestRepository;
import com.meesho.notificationservice.service.kafka.Producer;
import com.meesho.notificationservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsRequestService {
    @Autowired
    private SmsRequestRepository smsRequestRepository;
    @Autowired
    private Producer producer;

    public SuccessResponseEntity sendSms(SmsRequest smsRequest) throws BadRequestException, NotFoundException {

        isValidSmsRequest(smsRequest);
        smsRequest.setStatus("KAFKA_SEND");
        smsRequest.setCreatedAt(System.currentTimeMillis());
        smsRequestRepository.save(smsRequest);
        producer.sendMessage(smsRequest.getId());
        return new SuccessResponseEntity(smsRequest.getId(), "hi its successful");
    }

    public SmsRequest findSmsRequest(int id) throws BadRequestException, NotFoundException{

        SmsRequest smsRequested  = smsRequestRepository.findById(id);
        if(smsRequested == null){
            throw new NotFoundException("RequestId not found");
        }
        return smsRequested;
    }

    public void isValidSmsRequest(SmsRequest smsRequest) {
        if(!Utils.isValidPhoneNumber(smsRequest.getPhoneNumber()) || smsRequest.getMessage().equals("") ){
            throw new BadRequestException("Fields are not valid");
        }

    }
}
