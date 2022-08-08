package com.meesho.notificationservice.controllers;

import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.service.SmsRequestService;
import com.meesho.notificationservice.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sms")
public class SmsController {

    @Autowired
    private SmsRequestService smsRequestService;

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResponseEntity<Object> sendSms(@RequestBody SmsRequest smsRequest) throws BadRequestException {

        try{
            return  ResponseEntity.ok(smsRequestService.sendSms(smsRequest));
        }catch (BadRequestException e){
            return  ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST, "INVAlID_REQUEST", e.getMessage()));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Object> findSmsRequest(@PathVariable int id) throws BadRequestException, NotFoundException{

        try{
            return ResponseEntity.ok().body(smsRequestService.findSmsRequest(id));
        }catch (BadRequestException e){
            return  ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST, "INVAlID_REQUEST", e.getMessage()));
        }catch (NotFoundException e){
            return ResponseEntity.internalServerError().body(new Error(HttpStatus.NOT_FOUND, "NOT_FOUND", e.getMessage()));
        }
    }
}
