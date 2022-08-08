package com.meesho.notificationservice.models.request;


import com.meesho.notificationservice.models.BlacklistPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistPhoneNumberRequest {

    private List<BlacklistPhoneNumber> phone_numbers;

}
