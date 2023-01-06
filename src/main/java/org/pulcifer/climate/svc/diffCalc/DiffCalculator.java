package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;

import static java.lang.Math.abs;

public class DiffCalculator {
    public Float calcDiff(BigDecimal cityValue, BigDecimal similarCityValue) {
        if (cityValue == null || similarCityValue == null) return 1F;
        var diff = abs(cityValue.floatValue() - similarCityValue.floatValue());
        if (diff == 0F) return 1F;
        var r = diff / cityValue.floatValue();
        return r + 1.0F;
    }
}
