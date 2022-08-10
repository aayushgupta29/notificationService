package com.meesho.notificationservice.service;


import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.models.BlacklistPhoneNumber;
import com.meesho.notificationservice.models.response.BlackListResponse;
import com.meesho.notificationservice.repository.BlacklistRepository;
import com.meesho.notificationservice.repository.RedisManager;
import com.meesho.notificationservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlacklistService {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private RedisManager redisManager;

    public BlackListResponse addBlacklistNumbers(List<BlacklistPhoneNumber> phoneNumbers) throws BadRequestException, NotFoundException {

        if(phoneNumbers!= null && phoneNumbers.isEmpty()){
            throw new BadRequestException("Invalid List of phone numbers");
        }
        List<String> invalidPhoneNumbers = new ArrayList<>();

        for(BlacklistPhoneNumber phoneNumber: phoneNumbers){
            if(Utils.isValidPhoneNumber(phoneNumber.getPhoneNumber())){
                redisManager.addInCache(phoneNumber.getPhoneNumber());
                blacklistRepository.save(phoneNumber);
            }
            else{
                invalidPhoneNumbers.add(phoneNumber.getPhoneNumber());
            }
        }
        if(invalidPhoneNumbers.isEmpty()){
            return BlackListResponse.builder().message("new numbers added").build();
        }
        return BlackListResponse.builder().phoneNumbers(invalidPhoneNumbers).message("Phone Numbers are blacklisted except some of them").build();

    }



    public BlackListResponse getBlacklistNumbers() {

        Set<String> redisKeys = redisManager.getAllKeys();
        List<String> numbers = redisKeys.stream().collect(Collectors.toList());
        return  BlackListResponse.builder().phoneNumbers(numbers).build();

    }

    public BlackListResponse deleteBlacklistNumbers(List<BlacklistPhoneNumber> phoneNumbers) throws BadRequestException,NotFoundException{

        if(phoneNumbers!= null && phoneNumbers.isEmpty()){
            throw new BadRequestException("Invalid List of phone numbers");
        }
        List<String> invalidPhoneNumbers = new ArrayList<>();

        for(BlacklistPhoneNumber phoneNumber: phoneNumbers){
            if(Utils.isValidPhoneNumber(phoneNumber.getPhoneNumber())){
                redisManager.deleteFromCache((phoneNumber.getPhoneNumber()));
                blacklistRepository.deleteById(phoneNumber.getPhoneNumber());
            }
            else{
                invalidPhoneNumbers.add(phoneNumber.getPhoneNumber());
            }
        }
        if(invalidPhoneNumbers.isEmpty()){
            return BlackListResponse.builder().message("All numbers are whitelisted").build();
        }
        return BlackListResponse.builder().phoneNumbers(invalidPhoneNumbers).message("Phone Numbers are whitelisted except some of them").build();

    }
    public Boolean getStatusOfBlacklistNumber(String phoneNumber) {
        return redisManager.presentInCache(phoneNumber);
    }
}
