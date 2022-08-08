package com.meesho.notificationservice.repository;

import com.meesho.notificationservice.models.SmsRequest;
import org.springframework.data.repository.CrudRepository;

public interface SmsRequestRepository extends CrudRepository<SmsRequest, Integer> {
    public SmsRequest findById(int id);

}
