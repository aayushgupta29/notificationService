package com.meesho.notificationservice.service;

import com.meesho.notificationservice.models.SearchEntity;
import com.meesho.notificationservice.models.request.SearchRequest;
import com.meesho.notificationservice.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticSearchService {


    @Autowired
    private SearchRepository searchRepository;

    public List<SearchEntity> searchWithinTimeRange(SearchRequest request, int pageNumber) {

        List<SearchEntity> searchedMessages = searchRepository.findByPhoneNumberAndCreatedAtBetweenOrderByCreatedAtDesc(request.getPhoneNumber(),  request.getStartCreatedAt(), request.getEndCreatedAt(), PageRequest.of(pageNumber,50));
        return searchedMessages;
    }

    public void save(SearchEntity searchEntity) {
        searchRepository.save(searchEntity);
    }

    public List<SearchEntity> searchByMessage(SearchRequest request, int pageNumber) {

        List<SearchEntity> searchByMessage  = searchRepository.findByMessageContaining(request.getMessage(), PageRequest.of(pageNumber, 50));
        return  searchByMessage;
    }
}
