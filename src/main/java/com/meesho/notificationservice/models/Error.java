package com.meesho.notificationservice.models;


import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private HttpStatus status;
    private String code;
    private String message;

}
