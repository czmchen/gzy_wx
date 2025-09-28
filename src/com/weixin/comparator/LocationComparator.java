package com.weixin.comparator;

import java.util.Comparator;

import org.geotools.referencing.GeodeticCalculator;

import com.weixin.vo.RMWxUserCust;

public class LocationComparator implements Comparator<RMWxUserCust> {
    private RMWxUserCust referenceLocation;

    public LocationComparator(RMWxUserCust referenceLocation) {
        this.referenceLocation = referenceLocation;
    }

    @Override
    public int compare(RMWxUserCust loc1, RMWxUserCust loc2) {
        GeodeticCalculator gc = new GeodeticCalculator();
        gc.setStartingGeographicPoint(Double.valueOf(referenceLocation.getReLongitude()),Double.valueOf(referenceLocation.getReLatitude()));
        gc.setDestinationGeographicPoint(Double.valueOf(loc1.getReLongitude()), Double.valueOf(loc1.getReLatitude()));
        double distanceToLoc1 = gc.getOrthodromicDistance();
        gc.setDestinationGeographicPoint(Double.valueOf(loc2.getReLongitude()), Double.valueOf(loc2.getReLatitude()));
        double distanceToLoc2 = gc.getOrthodromicDistance();
        return Double.compare(distanceToLoc1, distanceToLoc2);
    }
}
