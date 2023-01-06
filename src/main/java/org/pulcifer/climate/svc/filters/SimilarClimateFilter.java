package org.pulcifer.climate.svc.filters;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimilarClimateFilter {

    public SimilarClimateFilter(BigDecimal similarityMultiplier) {
        this.similarityMultiplier = similarityMultiplier;
    }

    BigDecimal similarityMultiplier;

    public boolean filterOnlySimilarClimates(SimilarCity maybeSimilar, SimilarCity city) {
        BigDecimal minLowestTemperature = city.getLowestTemperature().divide(similarityMultiplier, RoundingMode.HALF_UP);
        BigDecimal maxHighestTemperature = city.getHighestTemperature().multiply(similarityMultiplier);
        return maybeSimilar.getLowestTemperature().compareTo(minLowestTemperature) > 0 && maybeSimilar.getHighestTemperature().compareTo(maxHighestTemperature) < 0;
    }

}
