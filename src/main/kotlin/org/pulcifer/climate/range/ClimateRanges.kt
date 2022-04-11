package org.pulcifer.climate.range

import org.pulcifer.climate.model.City
import java.math.BigDecimal
import java.util.Collections
import java.math.MathContext
import java.util.HashMap
import java.math.RoundingMode
import java.util.ArrayList

class ClimateRanges(city: City, val numOfOutliersToRemove: Int? = 0) {
    private val ranges: MutableMap<ClimateRangeType, Range> = HashMap()
    var reduction = BigDecimal(".9")
    var inflation = BigDecimal("1.2")
    var rainDayReduction = BigDecimal(".8")
    var rainDayInflation = BigDecimal("1.65")
    var rainfallReduction = BigDecimal(".8")
    var rainfallInflation = BigDecimal("1.3")
    var startIdx: Int
    var endIdx: Int
    private fun avgTemperature(avgTemps: List<BigDecimal>): Range {
        Collections.sort(avgTemps)
        val minAvgTemp = avgTemps[0].multiply(reduction, context).divide(TWO, context)
        val maxAvgTemp = avgTemps[avgTemps.size - 1].multiply(inflation, context).divide(TWO, context)
        return Range(minAvgTemp, maxAvgTemp)
    }

    private fun lowTemperature(lowTemps: List<BigDecimal?>): Range {
        Collections.sort(lowTemps)
        val minLowestTemp = lowTemps[0]!!.multiply(reduction, context)
        val maxLowestTemp = lowTemps[lowTemps.size - 1]!!.multiply(inflation, context)
        return Range(minLowestTemp, maxLowestTemp)
    }

    private fun highTemperature(highTemps: List<BigDecimal?>): Range {
        Collections.sort(highTemps)
        val minHighestTemp = highTemps[0]!!.multiply(reduction, context)
        val maxHighestTemp = highTemps[highTemps.size - 1]!!.multiply(inflation, context)
        return Range(minHighestTemp, maxHighestTemp)
    }

    private fun rainDays(rainDays: BigDecimal): Range {
        val minRainDays = rainDays.multiply(rainDayReduction, context)
        val maxRainDays = rainDays.multiply(rainDayInflation, context)
        return Range(minRainDays, maxRainDays)
    }

    private fun rainfall(rainfall: BigDecimal): Range {
        val minRainfall = rainfall.multiply(rainfallReduction, context)
        val maxRainfall = rainfall.multiply(rainfallInflation, context)
        return Range(minRainfall, maxRainfall)
    }

    val avgTemp: Range?
        get() = ranges[ClimateRangeType.AVG_TEMPERATURE]
    val highestTemp: Range?
        get() = ranges[ClimateRangeType.HIGHEST_TEMPERATURE]
    val lowestTemp: Range?
        get() = ranges[ClimateRangeType.LOWEST_TEMPERATURE]
    val rainDays: Range?
        get() = ranges[ClimateRangeType.RAIN_DAYS]
    val rainfall: Range?
        get() = ranges[ClimateRangeType.RAINFALL]

    companion object {
        private val context = MathContext(4, RoundingMode.HALF_UP)
        private val TWO = BigDecimal("2")
    }

    init {
        val avgTemps: MutableList<BigDecimal> = ArrayList()
        val lowTemps: MutableList<BigDecimal?> = ArrayList()
        val highTemps: MutableList<BigDecimal?> = ArrayList()
        var rainfall = BigDecimal.ZERO
        var rainDays = BigDecimal.ZERO
        for (m in city!!.climate!!.climateMonth!!) {
            avgTemps.add(m!!.minTempF!!.add(m!!.maxTempF))
            lowTemps.add(m.minTempF)
            highTemps.add(m.maxTempF)
            rainfall = rainfall.add(m.rainfall)
            rainDays = rainDays.add(m.raindays)
        }
        startIdx = numOfOutliersToRemove ?: 0
        // assuming # of months is 12
        endIdx = 12 - (numOfOutliersToRemove ?: 0) * 2
        ranges[ClimateRangeType.AVG_TEMPERATURE] = avgTemperature(avgTemps)
        ranges[ClimateRangeType.LOWEST_TEMPERATURE] = lowTemperature(lowTemps)
        ranges[ClimateRangeType.HIGHEST_TEMPERATURE] = highTemperature(highTemps)
        ranges[ClimateRangeType.RAIN_DAYS] = rainDays(rainDays)
        ranges[ClimateRangeType.RAINFALL] = rainfall(rainfall)
    }
}