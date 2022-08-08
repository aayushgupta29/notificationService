package com.meesho.notificationservice;


import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.models.response.SuccessResponseEntity;
import com.meesho.notificationservice.repository.SmsRequestRepository;
import com.meesho.notificationservice.service.SmsRequestService;
import com.meesho.notificationservice.service.kafka.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SmsRequestServiceTest {

    @Mock
    SmsRequestRepository smsRequestRepository;

    @Mock
    Producer producer;

    @InjectMocks
    SmsRequestService smsRequestService;

    @Test
    public void findSmsRequestTest(){
        SmsRequest smsRequest =  SmsRequest.builder().phoneNumber("+912345678901").message("Meesho").id(0).build();
        when(smsRequestRepository.findById(0)).thenReturn(smsRequest);
        try {
            assertEquals(smsRequest, smsRequestService.findSmsRequest(0));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void sendSmsTest()  {
        SmsRequest smsRequest = SmsRequest.builder().phoneNumber("+912345678901").message("Meesho").build();
        int smsRequestId = smsRequest.getId();
        SuccessResponseEntity successResponseEntity = new SuccessResponseEntity(smsRequestId, "hi its successful");
        Mockito.doNothing().when(producer).sendMessage(smsRequest.getId());
        when(smsRequestRepository.save(smsRequest)).thenReturn(smsRequest);
//        when(new SuccessResponseEntity(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenReturn(successResponseEntity);

        try {
            assertEquals(successResponseEntity.toString(), smsRequestService.sendSms(smsRequest).toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
