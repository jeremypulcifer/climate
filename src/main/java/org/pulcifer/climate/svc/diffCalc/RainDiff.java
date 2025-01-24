package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;

public class RainDiff implements Difference {
    public BigDecimal calcDiff(SimilarCity city, SimilarCity similarCity) {
        DiffCalculator diffCalculator = new DiffCalculator();
        var differenceAccumulated = BigDecimal.ZERO;
//        var diffRainDays = diffCalculator.calcDiff(city.getRainDays(), similarCity.getRainDays());
//        if (diffRainDays > 0) differenceAccumulated *= diffRainDays;
//        differenceAccumulated *= diffCalculator.calcDiff(city.getRainfall(), similarCity.getRainfall());
        return differenceAccumulated;
    }
}
