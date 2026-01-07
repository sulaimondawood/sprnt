package com.dawood.sprnt.infrastructure.redis.service;

import com.dawood.sprnt.common.config.CacheConfig;
import com.dawood.sprnt.driver.model.Driver;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.BoundingBox;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RedisMatchingService {


    private final RedisTemplate<String,Object> redisTemplate;
    private final GeometryFactory geometryFactory = new GeometryFactory();


//    public List<Driver> findDriversInGeofence(double lng,double lat,double expansion, int limit){
//
//        // 1. Parse the Geofence Polygon
//        Polygon geofence = (Polygon) new WKTReader().read(polygonWKT);
//
//        // 2. Get the Bounding Box (Envelope) of the Polygon
//        // We use this to ask Redis for a rough list of candidates
//        Envelope envelope = geofence.getEnvelopeInternal();
//        double centerLng = envelope.centre().x;
//        double centerLat = envelope.centre().y;
//        double widthKm = calculateDistance(envelope.getMinX(), centerLat, envelope.getMaxX(), centerLat);
//        double heightKm = calculateDistance(centerLng, envelope.getMinY(), centerLng, envelope.getMaxY());
//
//        // 3. REDIS: Box Search (The Rough Filter)
//        GeoResults<RedisGeoCommands.GeoLocation<String>> candidates = redisTemplate.opsForGeo()
//
//        GeoResults<RedisGeoCommands.GeoLocation<Object>> driverInRange = redisTemplate.opsForGeo()
//                .search(CacheConfig.DRIVER_GEO_KEY,
//                        GeoReference.fromCoordinate(new Point(lng, lat)),
//                        new BoundingBox(widthKm,heightKm, Metrics.KILOMETERS),
//                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
//                                .includeDistance()
//                                .limit(limit*2)
//                        );
//
//        if(driverInRange == null)return Collections.emptyList();
//
//        List<UUID> validDriverIds = new ArrayList<>();
//
//        // 4. JAVA: Precise Polygon Filter (The "Strainer")
//        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : candidates) {
//            Point redisPoint = result.getContent().getPoint();
//
//            // Convert Redis Point to JTS Point
//            org.locationtech.jts.geom.Point driverLocation = geometryFactory.createPoint(
//                    new Coordinate(redisPoint.getX(), redisPoint.getY())
//            );
//
//            // ✅ THE CHECK: Is this driver actually inside the irregular shape?
//            if (geofence.contains(driverLocation)) {
//                validDriverIds.add(UUID.fromString(result.getContent().getName()));
//            }
//        }
//
//        // 5. Fetch Details from Postgres
//        return driverRepository.findAllByIdIn(validDriverIds);
//
//    }
//
//    private double calculateDistance(double lng1, double lat1, double lng2, double lat2) {
//        // Simple implementation or use a library
//        return 111.0 * Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lng1 - lng2, 2));
//    }

}
