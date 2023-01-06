package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

public class RainDiff implements Difference {
    public float calcDiff(SimilarCity city, SimilarCity similarCity) {
        DiffCalculator diffCalculator = new DiffCalculator();
        var differenceAccumulated = 1F;
        var diffRainDays = diffCalculator.calcDiff(city.getRainDays(), similarCity.getRainDays());
        if (diffRainDays > 0) differenceAccumulated *= diffRainDays;
        differenceAccumulated *= diffCalculator.calcDiff(city.getRainfall(), similarCity.getRainfall());
        return differenceAccumulated;
    }
}
