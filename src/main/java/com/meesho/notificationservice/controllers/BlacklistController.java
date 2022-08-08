package com.meesho.notificationservice.controllers;


import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.models.Error;
import com.meesho.notificationservice.models.request.BlacklistPhoneNumberRequest;
import com.meesho.notificationservice.models.response.BlackListResponse;
import com.meesho.notificationservice.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/blacklist")
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;

    @PostMapping("")
    public ResponseEntity<Object> addBlacklistNumber(@RequestBody BlacklistPhoneNumberRequest phoneNumbers) throws BadRequestException, NotFoundException {

        try{
            return new ResponseEntity<>(blacklistService.addBlacklistNumbers(phoneNumbers.getPhone_numbers()), HttpStatus.OK);
        }catch (BadRequestException e){
            return  ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST, "INVAlID_REQUEST", e.getMessage()));
        }catch (NotFoundException e){
            return ResponseEntity.internalServerError().body(new Error(HttpStatus.BAD_REQUEST, "TIME_OUT", e.getMessage()));
        }

    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteBlacklistNumber(@RequestBody BlacklistPhoneNumberRequest phoneNumbers) throws NotFoundException, BadRequestException{

        try{
            return  new ResponseEntity<>(blacklistService.deleteBlacklistNumbers(phoneNumbers.getPhone_numbers()), HttpStatus.OK);
        }catch (BadRequestException e){
            return  ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST, "INVAlID_REQUEST", e.getMessage()));
        }catch (NotFoundException e){
            return ResponseEntity.internalServerError().body(new Error(HttpStatus.BAD_REQUEST, "TIME_OUT", e.getMessage()));
        }

    }

    @GetMapping("")
    public ResponseEntity<BlackListResponse> getAllBlacklistedNumbers() throws BadRequestException{

        try{
            return new ResponseEntity<>( blacklistService.getBlacklistNumbers(), HttpStatus.OK);
        }catch (BadRequestException e){
            return  ResponseEntity.badRequest().body(BlackListResponse.builder().error(e.getMessage()).build());
        }

    }


}
