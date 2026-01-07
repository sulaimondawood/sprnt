package com.dawood.sprnt.driver.service;

import com.dawood.sprnt.common.config.CacheConfig;
import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.repository.DriverRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverLocationService {

    private final DriverRepository driverRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final KafkaProducer kafkaProducer;

    public void processLocationUpdate(DriverLocationDTO location){

        redisTemplate.opsForGeo().add(
                CacheConfig.DRIVER_GEO_KEY,
                new Point(location.getLng(), location.getLat()),
                location.getDriverId().toString());

        kafkaProducer.sendDriverLocationUpdate(location);

    }

    public void removeDriverFromMap(String driverID){
        redisTemplate.opsForZSet().remove(CacheConfig.DRIVER_GEO_KEY, driverID);
    }

}
