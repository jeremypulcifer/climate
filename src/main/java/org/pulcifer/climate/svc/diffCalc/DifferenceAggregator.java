package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;

public class DifferenceAggregator implements Difference {

    public BigDecimal calcDiff(SimilarCity city, SimilarCity similarCity) {
        var differenceAccumulated = BigDecimal.ZERO;
        differenceAccumulated = differenceAccumulated.add(new AvgTempDiff().calcDiff(city, similarCity));
        differenceAccumulated = differenceAccumulated.add(new MinAndMaxTempDiff().calcDiff(city, similarCity));
        differenceAccumulated = differenceAccumulated.add(new RainDiff().calcDiff(city, similarCity));
        return differenceAccumulated;
    }
}
