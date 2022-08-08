package com.meesho.notificationservice.repository;

import com.meesho.notificationservice.models.BlacklistPhoneNumber;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistRepository extends CrudRepository<BlacklistPhoneNumber, String> {
}