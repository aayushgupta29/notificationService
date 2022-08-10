package com.meesho.notificationservice.service;

import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.models.BlacklistPhoneNumber;
import com.meesho.notificationservice.models.response.BlackListResponse;
import com.meesho.notificationservice.repository.BlacklistRepository;
import com.meesho.notificationservice.service.BlacklistService;
import com.meesho.notificationservice.repository.RedisManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlacklistServiceTest {
    @InjectMocks
    private BlacklistService blacklistService;
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private RedisManager redisManager;
    @Test
    public void getBlacklistNumbersTest(){
        when(redisManager.getAllKeys()).thenReturn(new HashSet<>(Collections.singletonList("+912345678901")));
        assertEquals(1, blacklistService.getBlacklistNumbers().getPhoneNumbers().size());
    }

    @Test
    public void addBlacklistNumbersTest()  {
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber("+912345678901");
        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = Collections.singletonList( blacklistPhoneNumber);
         blacklistService.addBlacklistNumbers(blacklistPhoneNumbers );

        verify(redisManager, times(1)).addInCache(blacklistPhoneNumbers.get(0).getPhoneNumber());
        verify(blacklistRepository, times(1)).save(blacklistPhoneNumbers.get(0));


    }

    @Test
    public void deleteBlacklistNumbersTest(){
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber("+912345678901");
        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = Collections.singletonList( blacklistPhoneNumber);
        blacklistService.deleteBlacklistNumbers(blacklistPhoneNumbers );

        verify(redisManager, times(1)).deleteFromCache(blacklistPhoneNumbers.get(0).getPhoneNumber());
        verify(blacklistRepository, times(1)).deleteById(blacklistPhoneNumbers.get(0).getPhoneNumber());
    }

    @Test
    public void addEmptyListTest()  {
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber("+912345678901");
        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = new ArrayList<BlacklistPhoneNumber>();
        assertThatThrownBy(() ->  blacklistService.addBlacklistNumbers(blacklistPhoneNumbers ))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid List of phone numbers");
    }

    @Test
    public void addInvalidListTest()  {
        String phoneNumber = "+912345678";
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber(phoneNumber);

        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = Collections.singletonList( blacklistPhoneNumber);

        BlackListResponse blackListResponse = blacklistService.addBlacklistNumbers(blacklistPhoneNumbers);

        BlackListResponse expectedBlackListResponse = BlackListResponse.builder().phoneNumbers(Collections.singletonList(phoneNumber)).message("Phone Numbers are blacklisted except some of them").build();

        assertEquals(expectedBlackListResponse, blackListResponse);
    }

    @Test
    public void deleteEmptyListTest()  {
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber("+912345678901");
        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = new ArrayList<BlacklistPhoneNumber>();
        assertThatThrownBy(() ->  blacklistService.deleteBlacklistNumbers(blacklistPhoneNumbers ))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid List of phone numbers");
    }

    @Test
    public void deleteInvalidListTest()  {
        String phoneNumber = "+912345678";
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber(phoneNumber);

        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = Collections.singletonList( blacklistPhoneNumber);

        BlackListResponse blackListResponse = blacklistService.deleteBlacklistNumbers(blacklistPhoneNumbers);

        BlackListResponse expectedBlackListResponse = BlackListResponse.builder().phoneNumbers(Collections.singletonList(phoneNumber)).message("Phone Numbers are whitelisted except some of them").build();

        assertEquals(expectedBlackListResponse, blackListResponse);
    }

    @Test
    public void ifBlacklistNumberPresent(){
        String phoneNumber = "+919876543210";
        when(redisManager.presentInCache(phoneNumber)).thenReturn(Boolean.TRUE);
        assertEquals(Boolean.TRUE,  blacklistService.getStatusOfBlacklistNumber(phoneNumber));
        verify(redisManager, times(1)).presentInCache(phoneNumber);
    }


}
