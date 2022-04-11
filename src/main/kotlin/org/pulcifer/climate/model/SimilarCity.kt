package org.pulcifer.climate.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import org.pulcifer.climate.range.ClimateRangeType
import java.math.BigDecimal
import java.util.stream.Collectors
import java.math.MathContext
import java.util.HashMap
import java.math.RoundingMode
import java.util.function.Function

@JsonInclude(JsonInclude.Include.NON_NULL)
class SimilarCity(c: City) {
    @JsonIgnore
    private val city: City

    @JsonIgnore
    private val climateData: Map<ClimateRangeType, List<BigDecimal?>>

    @get:JsonIgnore
    val avgTemperatures: List<BigDecimal?>
        get() = climateData[ClimateRangeType.AVG_TEMPERATURE]!!

    @get:JsonIgnore
    val lowestTemperatures: List<BigDecimal?>
        get() = climateData[ClimateRangeType.LOWEST_TEMPERATURE]!!

    @get:JsonIgnore
    val highestTemperatures: List<BigDecimal?>
        get() = climateData[ClimateRangeType.HIGHEST_TEMPERATURE]!!

    private fun extractListForRange(city: City, getValue: Function<ClimateMonth?, BigDecimal?>): List<BigDecimal?> {
        val climateMonths = city.climate?.climateMonth
        val values = climateMonths!!.stream().map { m: ClimateMonth? -> getValue.apply(m) }.collect(Collectors.toList())
        return values.sortedBy{it}
    }

    val cityName: String?
        get() = city.cityName
    val country: String?
        get() = city.country
    val highestAvgTemperature: BigDecimal?
        get() = if (avgTemperatures.isEmpty()) BigDecimal.ZERO else avgTemperatures[avgTemperatures.size - 1]
    val lowestAvgTemperature: BigDecimal?
        get() = if (avgTemperatures.isEmpty()) BigDecimal.ZERO else avgTemperatures[0]
    val avgTemperature: BigDecimal
        get() = if (avgTemperatures.isEmpty()) BigDecimal.ZERO else avgTemperatures.stream()
            .reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }!!
            .divide(
                BigDecimal(
                    avgTemperatures.size
                ), context
            )
    val lowestTemperature: BigDecimal?
        get() = if (lowestTemperatures.isEmpty()) BigDecimal.ZERO else lowestTemperatures[0]
    val highestTemperature: BigDecimal?
        get() = if (highestTemperatures.isEmpty()) BigDecimal.ZERO else highestTemperatures[highestTemperatures.size - 1]
    val rainDays: BigDecimal?
        get() = climateData[ClimateRangeType.RAIN_DAYS]!!
            .stream().reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }
    val rainfall: BigDecimal?
        get() = climateData[ClimateRangeType.RAINFALL]!!
            .stream().reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }

    companion object {
        private val context = MathContext(4, RoundingMode.HALF_UP)
        private val TWO = BigDecimal("2")
    }

    init {
        val climateData: MutableMap<ClimateRangeType, List<BigDecimal?>> = HashMap()
        climateData[ClimateRangeType.AVG_TEMPERATURE] = extractListForRange(c) { m: ClimateMonth? ->
            m?.minTempF?.add(m.maxTempF)?.divide(
                TWO, context
            )
        }
        climateData[ClimateRangeType.LOWEST_TEMPERATURE] =
            extractListForRange(c) { m: ClimateMonth? -> m?.minTempF }
        climateData[ClimateRangeType.HIGHEST_TEMPERATURE] =
            extractListForRange(c) { m: ClimateMonth? -> m?.maxTempF }
        climateData[ClimateRangeType.RAIN_DAYS] = extractListForRange(c) { m: ClimateMonth? -> m?.raindays }
        climateData[ClimateRangeType.RAINFALL] = extractListForRange(c) { m: ClimateMonth? -> m?.rainfall }
        this.city = c
        this.climateData = climateData
    }
}