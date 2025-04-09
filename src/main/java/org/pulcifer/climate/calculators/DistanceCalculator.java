package org.pulcifer.climate.calculators;

import org.pulcifer.climate.model.CityMeta;

public class DistanceCalculator {
//    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    public final static double AVERAGE_RADIUS_OF_EARTH_MI = 3963.1;

    public static Integer calculateDistanceBetweenCities(CityMeta aCity, CityMeta bCity) {
        return calculateDistanceInMiles(
                Double.parseDouble(aCity.getLatitude()), Double.parseDouble(aCity.getLongitude()),
                Double.parseDouble(bCity.getLatitude()), Double.parseDouble(bCity.getLongitude()));
    }

    private static int calculateDistanceInMiles(double aLatitude, double aLongitude,
                                            double bLatitude, double bLongitude) {

        double latDistance = Math.toRadians(aLatitude - bLatitude);
        double lngDistance = Math.toRadians(aLongitude - bLongitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(aLatitude)) * Math.cos(Math.toRadians(bLatitude))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_MI * c));
    }
}
