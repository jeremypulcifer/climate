package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

public class DifferenceAggregator implements Difference {

    public float calcDiff(SimilarCity city, SimilarCity similarCity) {
        var differenceAccumulated = 100.0F;
        differenceAccumulated *= new AvgTempDiff().calcDiff(city, similarCity);
        differenceAccumulated *= new MinAndMaxTempDiff().calcDiff(city, similarCity);
        differenceAccumulated *= new RainDiff().calcDiff(city, similarCity);
        return differenceAccumulated;
    }
}
