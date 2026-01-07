package com.dawood.sprnt.infrastructure.redis.service;

import com.dawood.sprnt.driver.model.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisMatchingService {

    private final RedisTemplate<String,Object> redisTemplate;

    public List<Driver> findDriversInRedis(double lng,double lat,double expansion, int limit){

//        List<Driver> driverInRange = redisTemplate.opsForGeo()
//                .search

    }

}
