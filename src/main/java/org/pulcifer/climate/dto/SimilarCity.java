package org.pulcifer.climate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.ClimateMonth;
import org.pulcifer.climate.range.ClimateRangeType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.pulcifer.climate.range.ClimateRangeType.AVG_TEMPERATURE;
import static org.pulcifer.climate.range.ClimateRangeType.HIGHEST_TEMPERATURE;
import static org.pulcifer.climate.range.ClimateRangeType.LOWEST_TEMPERATURE;
import static org.pulcifer.climate.range.ClimateRangeType.RAINFALL;
import static org.pulcifer.climate.range.ClimateRangeType.RAIN_DAYS;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimilarCity {
    private static MathContext context = new MathContext(4, RoundingMode.HALF_UP);
    private static BigDecimal TWO = new BigDecimal("2");

    @JsonIgnore
    private City city;
    @JsonIgnore
    private Map<ClimateRangeType, List<BigDecimal>> climateData;
    @JsonIgnore
    public List<BigDecimal> getAvgTemperatures() { return climateData.get(AVG_TEMPERATURE); }
    @JsonIgnore
    public List<BigDecimal> getLowestTemperatures() {
        return climateData.get(LOWEST_TEMPERATURE);
    }
    @JsonIgnore
    public List<BigDecimal> getHighestTemperatures() {
        return climateData.get(HIGHEST_TEMPERATURE);
    }

    public SimilarCity(City c) {
        Map<ClimateRangeType, List<BigDecimal>> climateData = new HashMap<>();
        climateData.put(AVG_TEMPERATURE, extractListForRange(c, (m) -> m.getMinTempF().add(m.getMaxTempF()).divide(TWO, context)));
        climateData.put(LOWEST_TEMPERATURE, extractListForRange(c, (m) -> m.getMinTempF()));
        climateData.put(HIGHEST_TEMPERATURE, extractListForRange(c, (m) -> m.getMaxTempF()));
        climateData.put(RAIN_DAYS, extractListForRange(c, (m) -> m.getRaindays()));
        climateData.put(RAINFALL, extractListForRange(c, (m) -> m.getRainfall()));
        this.city = city;
        this.climateData = climateData;
    }

    private List<BigDecimal> extractListForRange(City city, Function<ClimateMonth, BigDecimal> getValue) {
        List<ClimateMonth> climateMonths = city.getClimate().getClimateMonth();
        List<BigDecimal> values = climateMonths.stream().map(m -> getValue.apply(m)).collect(Collectors.toList());
        Collections.sort(values);
        return values;
    }

    public String getCityName() { return city.getCityName(); }
    public String getCountry() {
        return city.getCountry();
    }
    public BigDecimal getHighestAvgTemperature() {
        return getAvgTemperatures().get(getAvgTemperatures().size() - 1);
    }
    public BigDecimal getLowestAvgTemperature() {
        return getAvgTemperatures().get(0);
    }
    public BigDecimal getAvgTemperature() {
        return getAvgTemperatures().stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(getAvgTemperatures().size()), context);
    }
    public BigDecimal getLowestTemperature() {
        return getLowestTemperatures().get(0);
    }
    public BigDecimal getHighestTemperature() {
        return getHighestTemperatures().get(getHighestTemperatures().size() - 1);
    }
    public BigDecimal getRainDays() { return climateData.get(RAIN_DAYS).stream().reduce(BigDecimal.ZERO, BigDecimal::add); }
    public BigDecimal getRainfall() { return climateData.get(RAINFALL).stream().reduce(BigDecimal.ZERO, BigDecimal::add); }
}
