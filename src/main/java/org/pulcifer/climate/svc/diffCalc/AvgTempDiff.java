package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;

import static java.lang.Math.abs;

public class AvgTempDiff implements Difference {
    public float calcDiff(SimilarCity city, SimilarCity similarCity) {
        DiffCalculator diffCalculator = new DiffCalculator();
        var differenceAccumulated = 1F;
        var diffHighTemp = diffCalculator.calcDiff(city.getHighestAvgTemperature(), similarCity.getHighestAvgTemperature());
        if (diffHighTemp > 0) differenceAccumulated *= diffHighTemp;
        var diffLowTemp = diffCalculator.calcDiff(city.getLowestAvgTemperature(), similarCity.getLowestAvgTemperature());
        if (diffLowTemp > 0) differenceAccumulated *= diffLowTemp;
        return differenceAccumulated;
    }
}
