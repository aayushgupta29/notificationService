package com.meesho.notificationservice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Id;



@Document(indexName = "indexname1")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchEntity {

    @Id
    private  int id;
    private String phoneNumber;
    private String message;
    private long createdAt;
}
