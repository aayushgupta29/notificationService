package com.meesho.notificationservice.service;


import com.meesho.notificationservice.exceptions.BadRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.models.SmsRequest;
import com.meesho.notificationservice.models.response.SuccessResponseEntity;
import com.meesho.notificationservice.repository.SmsRequestRepository;
import com.meesho.notificationservice.service.kafka.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    public void findSmsRequestWithInValidIdTest(){
        SmsRequest smsRequest =  SmsRequest.builder().phoneNumber("+912345678901").message("Meesho").id(0).build();
        when(smsRequestRepository.findById(1)).thenReturn(null);
        assertThatThrownBy(() -> smsRequestService.findSmsRequest(1)).isInstanceOf(NotFoundException.class).hasMessage("RequestId not found");
    }



    @Test
    public void sendSmsTest()  {
        SmsRequest smsRequest = SmsRequest.builder().phoneNumber("+912345678901").message("Meesho").build();
        int smsRequestId = smsRequest.getId();
        SuccessResponseEntity successResponseEntity = new SuccessResponseEntity(smsRequestId, "hi its successful");
        Mockito.doNothing().when(producer).sendMessage(smsRequest.getId());
        when(smsRequestRepository.save(smsRequest)).thenReturn(smsRequest);
        SuccessResponseEntity response = smsRequestService.sendSms(smsRequest);
        assertEquals(successResponseEntity.toString(), response.toString());
    }

    @Test
    public void inValidPhoneNumberTest() {
        SmsRequest smsRequest = SmsRequest.builder().phoneNumber("+91234").message("meesho").build();
        assertThatThrownBy(() -> smsRequestService.sendSms(smsRequest)).isInstanceOf(BadRequestException.class).hasMessage("Fields are not valid");
    }

    @Test
    public void inValidMessageTest() {
        SmsRequest smsRequest = SmsRequest.builder().phoneNumber("+912340998765").message("").build();
        assertThatThrownBy(() -> smsRequestService.sendSms(smsRequest)).isInstanceOf(BadRequestException.class).hasMessage("Fields are not valid");
    }

}
