
package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CityWeather {

    private Integer queryCost;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String resolvedAddress;
    private String address;
    private String timezone;
    private Integer tzoffset;
    private String name;
    private String city;
    private String country;
    private Integer seedId;
    private List<DayWeather> days;

    public Integer getQueryCost() {
        return queryCost;
    }

    public void setQueryCost(Integer queryCost) {
        this.queryCost = queryCost;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getResolvedAddress() {
        return resolvedAddress;
    }

    public void setResolvedAddress(String resolvedAddress) {
        this.resolvedAddress = resolvedAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getSeedId() {
        return seedId;
    }

    public void setSeedId(Integer seedId) {
        this.seedId = seedId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getTzoffset() {
        return tzoffset;
    }

    public void setTzoffset(Integer tzoffset) {
        this.tzoffset = tzoffset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DayWeather> getDays() {
        return days;
    }

    public void setDays(List<DayWeather> days) {
        this.days = days;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
