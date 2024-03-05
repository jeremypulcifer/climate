package org.pulcifer.climate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.ClimateMonth;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityClimateBuilder {
    private static MathContext context = new MathContext(4, RoundingMode.HALF_UP);
    private static BigDecimal TWO = new BigDecimal("2");

    public Map<ClimateRangeType, List<BigDecimal>> buildCityClimate(City c) {
        Map<ClimateRangeType, List<BigDecimal>> climateData = new HashMap<>();
        climateData.put(ClimateRangeType.AVG_TEMPERATURE, extractListForRange(c, (m) -> m.getMinTempF().add(m.getMaxTempF()).divide(TWO, context)));
        climateData.put(ClimateRangeType.LOWEST_TEMPERATURE, extractListForRange(c, ClimateMonth::getMinTempF));
        climateData.put(ClimateRangeType.HIGHEST_TEMPERATURE, extractListForRange(c, ClimateMonth::getMaxTempF));
        climateData.put(ClimateRangeType.RAIN_DAYS, extractListForRange(c, ClimateMonth::getRaindays));
        climateData.put(ClimateRangeType.RAINFALL, extractListForRange(c, ClimateMonth::getRainfall));
        return climateData;
    }

    private List<BigDecimal> extractListForRange(City city, Function<ClimateMonth, BigDecimal> getValue) {
        List<ClimateMonth> climateMonths = city.getClimate().getClimateMonth();
        List<BigDecimal> values = climateMonths.stream().map(getValue::apply).filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(values);
        return values;
    }
}
