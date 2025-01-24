package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.abs;

public class DiffCalculator {
    public BigDecimal calcDiff(BigDecimal cityValue, BigDecimal similarCityValue) {
        if (cityValue == null || similarCityValue == null) return BigDecimal.ONE;
        if (similarCityValue.compareTo(cityValue) > 0) {
            return similarCityValue.divide(cityValue, 2, RoundingMode.HALF_UP);
        }
        return cityValue.divide(similarCityValue, 2, RoundingMode.HALF_UP);
    }
}
