package com.example.livelydarkness;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeFactory;

public class CalculateDistance {
    private static final ShapeFactory factory = SpatialContext.GEO.getShapeFactory(); // Factory class for creating geo shapes.

    /**
     * Calculate distance between two set of coordinates.
     * @param lat1 Latitude of first coordinates
     * @param lon1 Longitude of first coordinates
     * @param lat2 Latitude of second coordinates
     * @param lon2 Longitude of second coordinates
     * @return distance in meters
     */
    public double distance(double lat1, double lon1, double lat2, double lon2) {
        Point p1 = factory.pointXY(lon1, lat1);
        Point p2 = factory.pointXY(lon2, lat2);
        double deg = SpatialContext.GEO.calcDistance(p1, p2);
        return DistanceUtils.degrees2Dist(deg, DistanceUtils.EARTH_EQUATORIAL_RADIUS_KM * 1000);
    }
}
