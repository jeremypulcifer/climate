package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HourWeather {

    private String datetime;
    private Integer datetimeEpoch;
    private BigDecimal temp;
    private BigDecimal feelslike;
    private BigDecimal humidity;
    private BigDecimal dew;
    private Integer precip;
    private Integer precipprob;
    private Integer snow;
    private Integer snowdepth;
    private Object preciptype;
    private BigDecimal windgust;
    private BigDecimal windspeed;
    private Integer winddir;
    private BigDecimal pressure;
    private BigDecimal visibility;
    private BigDecimal cloudcover;
    private Integer solarradiation;
    private Integer solarenergy;
    private Integer uvindex;
    private String conditions;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getDatetimeEpoch() {
        return datetimeEpoch;
    }

    public void setDatetimeEpoch(Integer datetimeEpoch) {
        this.datetimeEpoch = datetimeEpoch;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getFeelslike() {
        return feelslike;
    }

    public void setFeelslike(BigDecimal feelslike) {
        this.feelslike = feelslike;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getDew() {
        return dew;
    }

    public void setDew(BigDecimal dew) {
        this.dew = dew;
    }

    public Integer getPrecip() {
        return precip;
    }

    public void setPrecip(Integer precip) {
        this.precip = precip;
    }

    public Integer getPrecipprob() {
        return precipprob;
    }

    public void setPrecipprob(Integer precipprob) {
        this.precipprob = precipprob;
    }

    public Integer getSnow() {
        return snow;
    }

    public void setSnow(Integer snow) {
        this.snow = snow;
    }

    public Integer getSnowdepth() {
        return snowdepth;
    }

    public void setSnowdepth(Integer snowdepth) {
        this.snowdepth = snowdepth;
    }

    public Object getPreciptype() {
        return preciptype;
    }

    public void setPreciptype(Object preciptype) {
        this.preciptype = preciptype;
    }

    public BigDecimal getWindgust() {
        return windgust;
    }

    public void setWindgust(BigDecimal windgust) {
        this.windgust = windgust;
    }

    public BigDecimal getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(BigDecimal windspeed) {
        this.windspeed = windspeed;
    }

    public Integer getWinddir() {
        return winddir;
    }

    public void setWinddir(Integer winddir) {
        this.winddir = winddir;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }

    public BigDecimal getVisibility() {
        return visibility;
    }

    public void setVisibility(BigDecimal visibility) {
        this.visibility = visibility;
    }

    public BigDecimal getCloudcover() {
        return cloudcover;
    }

    public void setCloudcover(BigDecimal cloudcover) {
        this.cloudcover = cloudcover;
    }

    public Integer getSolarradiation() {
        return solarradiation;
    }

    public void setSolarradiation(Integer solarradiation) {
        this.solarradiation = solarradiation;
    }

    public Integer getSolarenergy() {
        return solarenergy;
    }

    public void setSolarenergy(Integer solarenergy) {
        this.solarenergy = solarenergy;
    }

    public Integer getUvindex() {
        return uvindex;
    }

    public void setUvindex(Integer uvindex) {
        this.uvindex = uvindex;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
