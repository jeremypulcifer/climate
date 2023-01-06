package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

public class MinAndMaxTempDiff implements Difference {
    public float calcDiff(SimilarCity city, SimilarCity similarCity) {
        DiffCalculator diffCalculator = new DiffCalculator();
        float differenceAccumulated = 1F;
        differenceAccumulated *= diffCalculator.calcDiff(city.getAvgTemperature(), similarCity.getAvgTemperature());
        differenceAccumulated *= diffCalculator.calcDiff(city.getLowestTemperature(), similarCity.getLowestTemperature());
        differenceAccumulated *= diffCalculator.calcDiff(city.getHighestTemperature(), similarCity.getHighestTemperature());
        return differenceAccumulated;
    }
}
