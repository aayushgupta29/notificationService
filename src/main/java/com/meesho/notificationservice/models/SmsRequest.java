package com.meesho.notificationservice.models;


import lombok.*;

import javax.persistence.*;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Builder
@Table(name = "sms_request")
public class SmsRequest {

    @Id
    @GeneratedValue
    private  int id;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String message;
    private String status;
    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "failure_comments")
    private  String failureComments;
    
    @Column(name= "created_at")
    private long createdAt;
    @Column(name = "updated_at")
    private long updatedAt;


}
