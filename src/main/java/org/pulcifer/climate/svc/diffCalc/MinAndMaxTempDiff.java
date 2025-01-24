package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MinAndMaxTempDiff implements Difference {
    public BigDecimal calcDiff(SimilarCity city, SimilarCity similarCity) {
        DiffCalculator diffCalculator = new DiffCalculator();
        BigDecimal differenceAccumulated = BigDecimal.ZERO;
        List<BigDecimal> diffValues = new ArrayList<>();
        diffValues.add(diffCalculator.calcDiff(city.getAvgTemperature(), similarCity.getAvgTemperature()));
        diffValues.add(diffCalculator.calcDiff(city.getLowestTemperature(), similarCity.getLowestTemperature()));
        diffValues.add(diffCalculator.calcDiff(city.getHighestTemperature(), similarCity.getHighestTemperature()));
//        diffValues.add(diffCalculator.calcDiff(city.getRainDays(), similarCity.getRainDays()));
//        diffValues.add(diffCalculator.calcDiff(city.getRainfall(), similarCity.getRainfall()));


        for (BigDecimal value : diffValues) {
            if (value == BigDecimal.ZERO) {
                continue;
            }
            if (differenceAccumulated == BigDecimal.ZERO) {
                differenceAccumulated = value;
            } else {
                differenceAccumulated = differenceAccumulated.multiply(value);
            }
        }
        return differenceAccumulated;
    }
}
