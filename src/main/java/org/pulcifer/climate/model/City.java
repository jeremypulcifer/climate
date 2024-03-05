package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pulcifer.climate.model.serialization.CityJsonDeserializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = CityJsonDeserializer.class)
@Document(collection = "city")
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @Id
    public String id;
    private String lang;
    private String cityName;
    private String country;
    private BigDecimal cityLatitude;
    private BigDecimal cityLongitude;
    private Integer cityId;
    private Boolean isCapital;
    private String stationName;
    private String tourismURL;
    private String tourismBoardName;
    private Boolean isDep;
    private String timeZone;
    private String isDST;
    private Climate climate;
    private Integer population;

    private Range range;

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public enum Range {
        HOT(BigDecimal.valueOf(200)), MODERATE(BigDecimal.valueOf(76)), MILD(BigDecimal.valueOf(63)), COLD(BigDecimal.valueOf(52));

        final BigDecimal max;
        private Range(BigDecimal max) {
            this.max = max;
        }

        public static Range getTemperatureRange(BigDecimal temperature) {
            if (temperature.compareTo(COLD.max) <= 0) return COLD;
            if (temperature.compareTo(MILD.max) <= 0) return MILD;
            if (temperature.compareTo(MODERATE.max) <= 0) return MODERATE;
            return HOT;
        }
    }

    public City() {}

    public City(Integer cityId) {this.cityId = cityId;}

    public void setCountry(String country) {
        this.country = country;
    }
    public String getId() {return this.id;}
    public String getLang() {return this.lang;}
    public String getCityName() {return this.cityName;}
    public String getCountry() {return this.country;}
    public BigDecimal getCityLatitude() {return this.cityLatitude;}
    public BigDecimal getCityLongitude() {return this.cityLongitude;}
    public Integer getCityId() {return this.cityId;}
    public Boolean getIsCapital() {return this.isCapital;}
    public String getStationName() {return this.stationName;}
    public String getTourismURL() {return this.tourismURL;}
    public String getTourismBoardName() {return this.tourismBoardName;}
    public Boolean getIsDep() {return this.isDep;}
    public String getTimeZone() {return this.timeZone;}
    public String getIsDST() {return this.isDST;}
    public Climate getClimate() {return this.climate;}

    public boolean equals(Object b) {
        if (!(b instanceof City))
            return false;
        City cityB = (City)b;
        if (cityId == null || cityB.getCityId() == null)
            return false;
        return cityId.equals(cityB.getCityId());
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setClimate(Climate climate) {
        this.climate = climate;
    }

    public boolean hasMissingClimateData() {
        if (climate == null || climate.getClimateMonth() == null || climate.getClimateMonth().isEmpty()) return true;
        for (ClimateMonth cm : climate.getClimateMonth()) {
            if (cm.getMaxTempF() == null
                    || cm.getRaindays() == null
                    || cm.getRainfall() == null
                || cm.getMinTempF() == null)
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
