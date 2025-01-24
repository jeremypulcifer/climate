package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

import java.math.BigDecimal;

public interface Difference {
    BigDecimal calcDiff(SimilarCity city, SimilarCity similarCity);
}
