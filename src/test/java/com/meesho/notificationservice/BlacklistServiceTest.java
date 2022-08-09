package com.meesho.notificationservice;

import com.meesho.notificationservice.models.BlacklistPhoneNumber;
import com.meesho.notificationservice.repository.BlacklistRepository;
import com.meesho.notificationservice.service.BlacklistService;
import com.meesho.notificationservice.repository.RedisManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
        try {
            blacklistService.addBlacklistNumbers(blacklistPhoneNumbers );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        verify(redisManager, times(1)).addInCache(blacklistPhoneNumbers.get(0).getPhoneNumber(), "TRUE");
        verify(blacklistRepository, times(1)).save(blacklistPhoneNumbers.get(0));
    }

    @Test
    public void deleteBlacklistNumbersTest(){
        BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber("+912345678901");
        List<BlacklistPhoneNumber>  blacklistPhoneNumbers = Collections.singletonList( blacklistPhoneNumber);
        try {
            blacklistService.deleteBlacklistNumbers(blacklistPhoneNumbers );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        verify(redisManager, times(1)).deleteFromCache(blacklistPhoneNumbers.get(0).getPhoneNumber());
        verify(blacklistRepository, times(1)).deleteById(blacklistPhoneNumbers.get(0).getPhoneNumber());
    }
}
