package com.meesho.notificationservice.models.response;


import com.meesho.notificationservice.models.BlacklistPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackListResponse {

    @Nullable
    private List<String> phoneNumbers ;

    @Nullable
    private  String message;

    @Nullable
    private String error;



}
