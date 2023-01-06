package org.pulcifer.climate.svc.diffCalc;

import org.pulcifer.climate.dto.SimilarCity;

public interface Difference {
    float calcDiff(SimilarCity city, SimilarCity similarCity);
}
