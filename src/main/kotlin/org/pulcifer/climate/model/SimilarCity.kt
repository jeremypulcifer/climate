package org.pulcifer.climate.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import org.pulcifer.climate.range.ClimateRangeType
import java.math.BigDecimal
import java.util.stream.Collectors
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import java.util.function.Function

@JsonInclude(JsonInclude.Include.NON_NULL)
class SimilarCity(c: City, diff: Int) {
    @JsonIgnore
    private val city: City

    @JsonIgnore
    val diff: Int

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

    val cityId: Int?
        get() = city.cityId
    val cityName: String?
        get() = city.cityName
    val country: String?
        get() = city.country
    val highestAvgTemperature: BigDecimal?
        get() = getMax(avgTemperatures)
    val lowestAvgTemperature: BigDecimal?
        get() = getMin(avgTemperatures)
    val avgTemperature: BigDecimal
        get() = if (avgTemperatures.isEmpty()) BigDecimal.ZERO else avgTemperatures.stream()
            .reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }!!
            .divide(
                BigDecimal(
                    avgTemperatures.size
                ), context
            )
    val lowestTemperature: BigDecimal?
        get() = getMin(lowestTemperatures)
    val highestTemperature: BigDecimal?
        get() = getMax(highestTemperatures)
    val rainDays: BigDecimal?
        get() = climateData[ClimateRangeType.RAIN_DAYS]!!
            .stream().reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }
    val rainfall: BigDecimal?
        get() = climateData[ClimateRangeType.RAINFALL]!!
            .stream().reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }

    private fun getMax(l: List<BigDecimal?>): BigDecimal? {
        if (l.isEmpty()) return BigDecimal.ONE
        Collections.reverse(l)
        return l[0]
    }

    private fun getMin(l: List<BigDecimal?>): BigDecimal? {
        if (l.isEmpty()) return BigDecimal.ONE
        return l[0]
    }

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
        this.diff = diff
        this.climateData = climateData
    }
}