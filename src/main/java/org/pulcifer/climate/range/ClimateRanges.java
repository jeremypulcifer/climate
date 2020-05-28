package org.pulcifer.climate.range;

import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.ClimateMonth;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pulcifer.climate.range.ClimateRangeType.AVG_TEMPERATURE;
import static org.pulcifer.climate.range.ClimateRangeType.HIGHEST_TEMPERATURE;
import static org.pulcifer.climate.range.ClimateRangeType.LOWEST_TEMPERATURE;
import static org.pulcifer.climate.range.ClimateRangeType.RAINFALL;
import static org.pulcifer.climate.range.ClimateRangeType.RAIN_DAYS;

public class ClimateRanges {
    private static MathContext context = new MathContext(4, RoundingMode.HALF_UP);
    private static BigDecimal TWO = new BigDecimal("2");
    private Integer numOfOutliersToRemove;
    private Map<ClimateRangeType, Range> ranges = new HashMap<>();

    BigDecimal reduction = new BigDecimal("1");
    BigDecimal inflation = new BigDecimal("1.2");

    BigDecimal rainDayReduction = new BigDecimal("1");
    BigDecimal rainDayInflation = new BigDecimal("1.65");

    BigDecimal rainfallReduction = new BigDecimal("1");
    BigDecimal rainfallInflation = new BigDecimal("1.3");

    int startIdx;
    int endIdx;

    public ClimateRanges (City city, Integer numOfOutliersToRemove) {
        this.numOfOutliersToRemove = numOfOutliersToRemove;
        List<BigDecimal> avgTemps = new ArrayList<>();
        List<BigDecimal> lowTemps = new ArrayList<>();
        List<BigDecimal> highTemps = new ArrayList<>();
        BigDecimal rainfall = BigDecimal.ZERO;
        BigDecimal rainDays = BigDecimal.ZERO;
        for (ClimateMonth m : city.getClimate().getClimateMonth()) {
            avgTemps.add(m.getMinTempF().add(m.getMaxTempF()));
            lowTemps.add(m.getMinTempF());
            highTemps.add(m.getMaxTempF());
            rainfall = rainfall.add(m.getRainfall());
            rainDays = rainDays.add(m.getRaindays());
        }
        startIdx = numOfOutliersToRemove;
        // assuming # of months is 12
        endIdx = 12 - (numOfOutliersToRemove * 2);
        ranges.put(AVG_TEMPERATURE, avgTemperature(avgTemps));
        ranges.put(LOWEST_TEMPERATURE, lowTemperature(lowTemps));
        ranges.put(HIGHEST_TEMPERATURE, highTemperature(highTemps));
        ranges.put(RAIN_DAYS, rainDays(rainDays));
        ranges.put(RAINFALL, rainfall(rainfall));
    }

    private Range avgTemperature(List<BigDecimal> avgTemps) {
        Collections.sort(avgTemps);
        BigDecimal minAvgTemp = avgTemps.get(startIdx).multiply(reduction, context).divide(TWO, context);
        BigDecimal maxAvgTemp = avgTemps.get(endIdx).multiply(inflation, context).divide(TWO, context);
        return new Range(minAvgTemp, maxAvgTemp);
    }

    private Range lowTemperature(List<BigDecimal> lowTemps) {
        Collections.sort(lowTemps);
        BigDecimal minLowestTemp = lowTemps.get(startIdx).multiply(reduction, context);
        BigDecimal maxLowestTemp = lowTemps.get(endIdx).multiply(inflation, context);
        return new Range(minLowestTemp, maxLowestTemp);
    }

    private Range highTemperature(List<BigDecimal> highTemps) {
        Collections.sort(highTemps);
        BigDecimal minHighestTemp = highTemps.get(startIdx).multiply(reduction, context);
        BigDecimal maxHighestTemp = highTemps.get(endIdx).multiply(inflation, context);
        return new Range(minHighestTemp, maxHighestTemp);
    }

    private Range rainDays(BigDecimal rainDays) {
        BigDecimal minRainDays = rainDays.multiply(rainDayReduction, context);
        BigDecimal maxRainDays = rainDays.multiply(rainDayInflation, context);
        return new Range(minRainDays, maxRainDays);
    }

    private Range rainfall(BigDecimal rainfall) {
        BigDecimal minRainfall = rainfall.multiply(rainfallReduction, context);
        BigDecimal maxRainfall = rainfall.multiply(rainfallInflation, context);
        return new Range(minRainfall, maxRainfall);
    }

    public Integer getNumOfOutliersToRemove() { return numOfOutliersToRemove; }
    public Range getAvgTemp() { return ranges.get(AVG_TEMPERATURE); }
    public Range getHighestTemp() { return ranges.get(HIGHEST_TEMPERATURE); }
    public Range getLowestTemp() { return ranges.get(LOWEST_TEMPERATURE); }
    public Range getRainDays() { return ranges.get(RAIN_DAYS); }
    public Range getRainfall() { return ranges.get(RAINFALL); }
}
