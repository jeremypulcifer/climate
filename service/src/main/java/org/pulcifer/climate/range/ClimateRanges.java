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

        BigDecimal reduction = new BigDecimal(".9");
        BigDecimal inflation = new BigDecimal("1.3");

        BigDecimal rainDayReduction = new BigDecimal("1");
        BigDecimal rainDayInflation = new BigDecimal("1.8");

        BigDecimal rainfallReduction = new BigDecimal("1");
        BigDecimal rainfallInflation = new BigDecimal("1.3");

        Collections.sort(avgTemps);
        BigDecimal minAvgTemp = avgTemps.get(1).multiply(reduction, context).divide(TWO, context);
        BigDecimal maxAvgTemp = avgTemps.get(10).multiply(inflation, context).divide(TWO, context);
        ranges.put(AVG_TEMPERATURE, new Range(minAvgTemp, maxAvgTemp));

        Collections.sort(lowTemps);
        BigDecimal minLowestTemp = lowTemps.get(1).multiply(reduction, context);
        BigDecimal maxLowestTemp = lowTemps.get(10).multiply(inflation, context);
        ranges.put(LOWEST_TEMPERATURE, new Range(minLowestTemp, maxLowestTemp));

        Collections.sort(highTemps);
        BigDecimal minHighestTemp = highTemps.get(1).multiply(reduction, context);
        BigDecimal maxHighestTemp = highTemps.get(10).multiply(inflation, context);
        ranges.put(HIGHEST_TEMPERATURE, new Range(minHighestTemp, maxHighestTemp));

        BigDecimal minRainDays = rainDays.multiply(rainDayReduction, context);
        BigDecimal maxRainDays = rainDays.multiply(rainDayInflation, context);
        ranges.put(RAIN_DAYS, new Range(minRainDays, maxRainDays));

        BigDecimal minRainfall = rainfall.multiply(rainfallReduction, context);
        BigDecimal maxRainfall = rainfall.multiply(rainfallInflation, context);
        ranges.put(RAINFALL, new Range(minRainfall, maxRainfall));
    }

    public Integer getNumOfOutliersToRemove() {
        return numOfOutliersToRemove;
    }
    public Range getAvgTemp() { return ranges.get(AVG_TEMPERATURE); }
    public Range getHighestTemp() {
        return ranges.get(HIGHEST_TEMPERATURE);
    }
    public Range getLowestTemp() {
        return ranges.get(LOWEST_TEMPERATURE);
    }
    public Range getRainDays() { return ranges.get(RAIN_DAYS); }
    public Range getRainfall() {
        return ranges.get(RAINFALL);
    }
}

