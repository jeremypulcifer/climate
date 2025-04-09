package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DayWeather {

    private String datetime;
    private Integer datetimeEpoch;
    private BigDecimal tempmax;
    private BigDecimal tempmin;
    private BigDecimal temp;
    private BigDecimal feelslikemax;
    private Integer feelslikemin;
    private BigDecimal feelslike;
    private BigDecimal dew;
    private BigDecimal humidity;
    private Integer precip;
    private Integer precipprob;
    private Integer precipcover;
    private Object preciptype;
    private Integer snow;
    private Integer snowdepth;
    private BigDecimal windgust;
    private BigDecimal windspeed;
    private BigDecimal winddir;
    private BigDecimal pressure;
    private BigDecimal cloudcover;
    private BigDecimal visibility;
    private BigDecimal solarradiation;
    private BigDecimal solarenergy;
    private Integer uvindex;
    private String sunrise;
    private Integer sunriseEpoch;
    private String sunset;
    private Integer sunsetEpoch;
    private BigDecimal moonphase;
    private String conditions;
    private String description;
    private List<HourWeather> hours;

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

    public BigDecimal getTempmax() {
        return tempmax;
    }

    public void setTempmax(BigDecimal tempmax) {
        this.tempmax = tempmax;
    }

    public BigDecimal getTempmin() {
        return tempmin;
    }

    public void setTempmin(BigDecimal tempmin) {
        this.tempmin = tempmin;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getFeelslikemax() {
        return feelslikemax;
    }

    public void setFeelslikemax(BigDecimal feelslikemax) {
        this.feelslikemax = feelslikemax;
    }

    public Integer getFeelslikemin() {
        return feelslikemin;
    }

    public void setFeelslikemin(Integer feelslikemin) {
        this.feelslikemin = feelslikemin;
    }

    public BigDecimal getFeelslike() {
        return feelslike;
    }

    public void setFeelslike(BigDecimal feelslike) {
        this.feelslike = feelslike;
    }

    public BigDecimal getDew() {
        return dew;
    }

    public void setDew(BigDecimal dew) {
        this.dew = dew;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
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

    public Integer getPrecipcover() {
        return precipcover;
    }

    public void setPrecipcover(Integer precipcover) {
        this.precipcover = precipcover;
    }

    public Object getPreciptype() {
        return preciptype;
    }

    public void setPreciptype(Object preciptype) {
        this.preciptype = preciptype;
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

    public BigDecimal getWinddir() {
        return winddir;
    }

    public void setWinddir(BigDecimal winddir) {
        this.winddir = winddir;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }

    public BigDecimal getCloudcover() {
        return cloudcover;
    }

    public void setCloudcover(BigDecimal cloudcover) {
        this.cloudcover = cloudcover;
    }

    public BigDecimal getVisibility() {
        return visibility;
    }

    public void setVisibility(BigDecimal visibility) {
        this.visibility = visibility;
    }

    public BigDecimal getSolarradiation() {
        return solarradiation;
    }

    public void setSolarradiation(BigDecimal solarradiation) {
        this.solarradiation = solarradiation;
    }

    public BigDecimal getSolarenergy() {
        return solarenergy;
    }

    public void setSolarenergy(BigDecimal solarenergy) {
        this.solarenergy = solarenergy;
    }

    public Integer getUvindex() {
        return uvindex;
    }

    public void setUvindex(Integer uvindex) {
        this.uvindex = uvindex;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getSunriseEpoch() {
        return sunriseEpoch;
    }

    public void setSunriseEpoch(Integer sunriseEpoch) {
        this.sunriseEpoch = sunriseEpoch;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Integer getSunsetEpoch() {
        return sunsetEpoch;
    }

    public void setSunsetEpoch(Integer sunsetEpoch) {
        this.sunsetEpoch = sunsetEpoch;
    }

    public BigDecimal getMoonphase() {
        return moonphase;
    }

    public void setMoonphase(BigDecimal moonphase) {
        this.moonphase = moonphase;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<HourWeather> getHours() {
        return hours;
    }

    public void setHours(List<HourWeather> hours) {
        this.hours = hours;
    }

    public boolean hadPrecip() {
        return precip != null && precip > 0;
    }
}
