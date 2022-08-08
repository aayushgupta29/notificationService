package com.meesho.notificationservice;


import com.meesho.notificationservice.models.SearchEntity;
import com.meesho.notificationservice.models.request.SearchRequest;
import com.meesho.notificationservice.repository.SearchRepository;
import com.meesho.notificationservice.service.ElasticSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchServiceTest {


    @InjectMocks
    ElasticSearchService elasticSearchService;

    @Mock
    SearchRepository searchRepository;



    @Test
    public void searchWithinTimeRangeTest(){

        SearchRequest searchRequest = SearchRequest.builder().phoneNumber("+912345678901").startCreatedAt(1234).endCreatedAt(2345).build();
        SearchEntity searchEntity = SearchEntity.builder().phoneNumber("+912345678901").build();

        when(searchRepository.findByPhoneNumberAndCreatedAtBetweenOrderByCreatedAtDesc(searchRequest.getPhoneNumber(), searchRequest.getStartCreatedAt(), searchRequest.getEndCreatedAt(), PageRequest.of(0,50)))
                .thenReturn(Collections.singletonList(searchEntity));

        assertEquals(1 ,elasticSearchService.searchWithinTimeRange(searchRequest).size() );

    }

    @Test
    public void searchByMessageTest(){

        SearchRequest searchRequest = SearchRequest.builder().message("Meesho").build();
        SearchEntity searchEntity = SearchEntity.builder().phoneNumber("+912345678901").build();

        when(searchRepository.findByMessageContaining(searchRequest.getMessage() , PageRequest.of(0,50)))
                .thenReturn(Collections.singletonList(searchEntity));

        assertEquals(1 ,elasticSearchService.searchByMessage(searchRequest).size() );

    }

}
