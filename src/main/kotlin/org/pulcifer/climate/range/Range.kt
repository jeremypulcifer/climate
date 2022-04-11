package org.pulcifer.climate.range

import java.math.BigDecimal

class Range(val min: BigDecimal, val max: BigDecimal) {
    override fun toString(): String {
        return String.format("%s::%s", min, max)
    }
}