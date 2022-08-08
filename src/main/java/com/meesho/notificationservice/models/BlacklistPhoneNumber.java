package com.meesho.notificationservice.models;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blacklisted_numbers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlacklistPhoneNumber {

    @Id
    private String phoneNumber;


}
