package org.pulcifer.climate.range;

import java.math.BigDecimal;

public class Range {
    private BigDecimal min;
    private BigDecimal max;
    public Range(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }
    public BigDecimal getMax() {
        return max;
    }

    public String toString() {
        return String.format("%s::%s", min, max);
    }
}
