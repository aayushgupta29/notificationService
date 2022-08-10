package com.meesho.notificationservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class RedisManager {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public void addInCache(String key){
        redisTemplate.opsForValue().set(key, "TRUE");
    }

    public void deleteFromCache(String key){
        redisTemplate.delete(key);
    }

    public boolean presentInCache(String key){
        return redisTemplate.opsForValue().get(key) != null;
    }

    public Set<String> getAllKeys(){
        return redisTemplate.keys("+*");
    }
}
