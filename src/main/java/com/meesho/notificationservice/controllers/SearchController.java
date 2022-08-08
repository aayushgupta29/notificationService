package com.meesho.notificationservice.controllers;


import com.meesho.notificationservice.models.SearchEntity;
import com.meesho.notificationservice.models.request.SearchRequest;
import com.meesho.notificationservice.models.response.SearchResponse;
import com.meesho.notificationservice.repository.SearchRepository;
import com.meesho.notificationservice.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/search")
public class SearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private SearchRepository searchRepository;

    @GetMapping("/time")
    public ResponseEntity<SearchResponse> searchWithinTimeRange(@RequestBody SearchRequest request){
        List<SearchEntity> allMessagesInTimeRange = elasticSearchService.searchWithinTimeRange(request);

        return new ResponseEntity<>(new SearchResponse(allMessagesInTimeRange), HttpStatus.OK);

    }

    @GetMapping("/message")
    public ResponseEntity<SearchResponse> searchByMessage(@RequestBody SearchRequest request){
        List<SearchEntity> searchedMessages = elasticSearchService.searchByMessage(request);

        return new ResponseEntity<>(new SearchResponse(searchedMessages), HttpStatus.OK);

    }

    @GetMapping("/getAll")
    public ResponseEntity<Iterable<SearchEntity> > findAll(){
        return new ResponseEntity<>(searchRepository.findAll(), HttpStatus.OK);
    }
}
