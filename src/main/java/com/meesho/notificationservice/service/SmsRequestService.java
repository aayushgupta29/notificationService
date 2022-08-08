package com.meesho.notificationservice.service;

import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.models.response.SuccessResponseEntity;
import com.meesho.notificationservice.repository.SmsRequestRepository;
import com.meesho.notificationservice.service.kafka.Producer;
import com.meesho.notificationservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

        try{
            smsRequestRepository.save(smsRequest);
        }catch (DataAccessException e){
            throw new BadRequestException("Internal server error");
        }

        producer.sendMessage(smsRequest.getId());
        return new SuccessResponseEntity(smsRequest.getId(), "hi its successful");
    }

    public SmsRequest findSmsRequest(int id) throws BadRequestException, NotFoundException{

        SmsRequest smsRequested = new SmsRequest();
        try{
            smsRequested = smsRequestRepository.findById(id);
        }catch (DataAccessException e){
            throw new BadRequestException("Internal server error");
        }

        if(smsRequested == null){
            throw new NotFoundException("RequestId not found");
        }

        return smsRequested;

    }

    public void isValidSmsRequest(SmsRequest smsRequest) {
        if(!Utils.isValidPhoneNumber(smsRequest.getPhoneNumber())){
            throw new BadRequestException("Phone no is not valid ");
        }
        if(smsRequest.getMessage().equals("") ){
            throw new BadRequestException("Message is missing or empty ");
        }

    }
}
