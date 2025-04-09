package org.pulcifer.climate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.pulcifer.climate.model.City;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimilarCity {
    private static final MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);

    @JsonIgnore
    private City city;
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
    public enum ScoreTypes {
        RAINFALL, RAIN_DAYS,
        AVG_TEMP, LOWEST_AVG_TEMP, HIGHEST_AVG_TEMP,
        HIGHEST_TEMP, LOWEST_TEMP;
    }

    private Map<ScoreTypes, BigDecimal> ranks;

    @JsonInclude
    public Map<ScoreTypes, BigDecimal> getRanks() {
        return ranks;
    }

    public SimilarCity(City c) {
        this.city = c;
//        this.climateData = new CityClimateBuilder().buildCityClimate(city);
        this.ranks = new HashMap<>();
    }

    public String getCityName() { return city.getCityName(); }
    public Integer getCityId() { return city.getCityId(); }
    public String getCountry() {
        return city.getCountry();
    }
    public Integer getPopulation() {
        return city.getPopulation();
    }
    public String getLatitude() { return city.getCityLatitude(); }
    public String getLongitude() { return city.getCityLongitude(); }
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
                .divide(new BigDecimal(getAvgTemperatures().size()), mathContext);
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

    public BigDecimal getTotalRank() {
        BigDecimal score = BigDecimal.ONE;
        for (BigDecimal rank : ranks.values()) {
            if (BigDecimal.ZERO.equals(rank)) continue;
            score = score.add(rank);//.sqrt(mathContext));
        }
        return score;
    }

//    public BigDecimal getTotalRank() {
//        BigDecimal score = BigDecimal.ONE;
//        for (BigDecimal rank : ranks.values()) {
//            if (BigDecimal.ZERO.equals(rank)) continue;
//            score = score.multiply(rank);//.sqrt(mathContext));
//        }
//        return score;
//    }

    public void setRainfallRank(BigDecimal rainfallRank) {
        ranks.put(ScoreTypes.RAINFALL, rainfallRank);
    }

    public void setRainDaysRank(BigDecimal rainDaysRank) {
        ranks.put(ScoreTypes.RAIN_DAYS, rainDaysRank);
    }

    public void setHighestAvgTemperatureRank(BigDecimal highestAvgTemperatureRank) {
        ranks.put(ScoreTypes.HIGHEST_AVG_TEMP, highestAvgTemperatureRank);
    }

    public void setLowestAvgTemperatureRank(BigDecimal lowestAvgTemperatureRank) {
        ranks.put(ScoreTypes.LOWEST_AVG_TEMP, lowestAvgTemperatureRank);
    }

    public void setAvgTemperatureRank(BigDecimal avgTemperatureRank) {
        ranks.put(ScoreTypes.AVG_TEMP, avgTemperatureRank);
    }

    public void setHighestTemperatureRank(BigDecimal highestTemperatureRank) {
        ranks.put(ScoreTypes.HIGHEST_TEMP, highestTemperatureRank);
    }

    public void setLowestTemperatureRank(BigDecimal lowestTemperatureRank) {
        ranks.put(ScoreTypes.LOWEST_TEMP, lowestTemperatureRank);
    }
}
