package org.pulcifer.climate.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
class Climate {
    var raintype: String? = null
    var raindef: String? = null
    var rainunit: String? = null
    var datab: BigDecimal? = null
    var datae: BigDecimal? = null
    var tempb: BigDecimal? = null
    var tempe: BigDecimal? = null
    var rdayb: BigDecimal? = null
    var rdaye: BigDecimal? = null
    var rainfallb: BigDecimal? = null
    var rainfalle: BigDecimal? = null
    var climatefromclino: String? = null
    var climateMonth: List<ClimateMonth?>? = null
}