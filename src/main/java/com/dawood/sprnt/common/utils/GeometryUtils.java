package com.dawood.sprnt.common.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {

    public static Point createPoint(double lng, double lat) {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

        Coordinate coordinate = new Coordinate(lng, lat);

        return factory.createPoint(coordinate);

    }

}
