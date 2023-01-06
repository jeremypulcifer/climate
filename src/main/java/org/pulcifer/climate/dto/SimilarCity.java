package org.pulcifer.climate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.pulcifer.climate.model.City;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimilarCity {
    private static MathContext context = new MathContext(4, RoundingMode.HALF_UP);
    private static BigDecimal TWO = new BigDecimal("2");

    @JsonIgnore
    private City city;
    private Integer diff;
    @JsonIgnore
    private Map<ClimateRangeType, List<BigDecimal>> climateData;
    @JsonIgnore
    public List<BigDecimal> getAvgTemperatures() { return climateData.get(ClimateRangeType.AVG_TEMPERATURE); }
    @JsonIgnore
    public List<BigDecimal> getLowestTemperatures() {
        return climateData.get(ClimateRangeType.LOWEST_TEMPERATURE);
    }
    @JsonIgnore
    public List<BigDecimal> getHighestTemperatures() {
        return climateData.get(ClimateRangeType.HIGHEST_TEMPERATURE);
    }

    public SimilarCity(City c) {
        this.city = c;
        this.climateData = new CityClimateBuilder().buildCityClimate(city);
    }

    public SimilarCity setDiffScore(Integer diff) {
        this.diff = diff;
        return this;
    }

    public String getCityName() { return city.getCityName(); }
    public Integer getCityId() { return city.getCityId(); }
    public Integer getDiff() { return diff; }
    public String getCountry() {
        return city.getCountry();
    }
    public Integer getPopulation() {
        return city.getPopulation();
    }
    public BigDecimal getHighestAvgTemperature() {
        if (getAvgTemperatures().isEmpty()) return new BigDecimal("-1");
        return getAvgTemperatures().get(getAvgTemperatures().size() - 1);
    }
    public BigDecimal getLowestAvgTemperature() {
        if (getAvgTemperatures().isEmpty()) return new BigDecimal("-1");
        return getAvgTemperatures().get(0);
    }
    public BigDecimal getAvgTemperature() {
        if (getAvgTemperatures().isEmpty()) return new BigDecimal("-1");
        return getAvgTemperatures().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(getAvgTemperatures().size()), context);
    }
    public BigDecimal getLowestTemperature() {
        if (getLowestTemperatures().isEmpty()) return new BigDecimal("-1");
        return getLowestTemperatures().get(0);
    }
    public BigDecimal getHighestTemperature() {
        if (getHighestTemperatures().isEmpty()) return new BigDecimal("-1");
        return getHighestTemperatures().get(getHighestTemperatures().size() - 1);
    }
    public BigDecimal getRainDays() { return climateData.get(ClimateRangeType.RAIN_DAYS).stream().reduce(BigDecimal.ZERO, BigDecimal::add); }
    public BigDecimal getRainfall() { return climateData.get(ClimateRangeType.RAINFALL).stream().reduce(BigDecimal.ZERO, BigDecimal::add); }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimilarCity)) return false;
        SimilarCity thatSC = (SimilarCity)obj;
        return thatSC.getCityId().equals(getCityId());
    }
}
