package com.meesho.notificationservice.controllers;


import com.meesho.notificationservice.models.SearchEntity;
import com.meesho.notificationservice.models.request.SearchRequest;
import com.meesho.notificationservice.models.response.SearchResponse;
import com.meesho.notificationservice.repository.SearchRepository;
import com.meesho.notificationservice.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/search")
public class SearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private SearchRepository searchRepository;

    @GetMapping("/time/{pageNumber}")
    public ResponseEntity<SearchResponse> searchWithinTimeRange(@RequestBody SearchRequest request, @PathVariable int pageNumber){
        List<SearchEntity> allMessagesInTimeRange = elasticSearchService.searchWithinTimeRange(request, pageNumber);

        return new ResponseEntity<>(new SearchResponse(allMessagesInTimeRange), HttpStatus.OK);

    }

    @GetMapping("/message/{pageNumber}")
    public ResponseEntity<SearchResponse> searchByMessage(@RequestBody SearchRequest request, @PathVariable int pageNumber){
        List<SearchEntity> searchedMessages = elasticSearchService.searchByMessage(request, pageNumber);

        return new ResponseEntity<>(new SearchResponse(searchedMessages), HttpStatus.OK);

    }

    @GetMapping("/getAll")
    public ResponseEntity<Iterable<SearchEntity> > findAll(){
        return new ResponseEntity<>(searchRepository.findAll(), HttpStatus.OK);
    }
}
