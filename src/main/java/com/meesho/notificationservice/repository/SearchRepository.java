package com.meesho.notificationservice.repository;

import com.meesho.notificationservice.models.SearchEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface  SearchRepository extends ElasticsearchRepository<SearchEntity, Integer> {
    List<SearchEntity> findByPhoneNumberAndCreatedAtBetweenOrderByCreatedAtDesc(String phoneNumber, long startCreatedAt, long endCreatedAt , Pageable pageable);
    List<SearchEntity> findByMessageContaining(String message, Pageable pageable);
}
