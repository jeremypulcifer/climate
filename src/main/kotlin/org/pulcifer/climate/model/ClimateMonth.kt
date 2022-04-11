package org.pulcifer.climate.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
class ClimateMonth {
    var month: Int? = null
    var maxTemp: BigDecimal? = null
    var minTemp: BigDecimal? = null
    var meanTemp: BigDecimal? = null
    var maxTempF: BigDecimal? = null
    var minTempF: BigDecimal? = null
    var meanTempF: BigDecimal? = null
    var raindays: BigDecimal? = null
    var rainfall: BigDecimal? = null
}