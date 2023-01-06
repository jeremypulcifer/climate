package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClimateMonth {
    private Integer month;
    private BigDecimal maxTemp;
    private BigDecimal minTemp;
    private BigDecimal meanTemp;
    private BigDecimal maxTempF;
    private BigDecimal minTempF;
    private BigDecimal meanTempF;
    private BigDecimal raindays;
    private BigDecimal rainfall;

    public ClimateMonth() {}

    public Integer getMonth() {return this.month;}
    public BigDecimal getMaxTemp() {return this.maxTemp;}
    public BigDecimal getMinTemp() {return this.minTemp;}
    public BigDecimal getMeanTemp() {return this.meanTemp;}
    public BigDecimal getMaxTempF() {return this.maxTempF;}
    public BigDecimal getMinTempF() {return this.minTempF;}
    public BigDecimal getMeanTempF() {return this.meanTempF;}
    public BigDecimal getRaindays() {return this.raindays;}
    public BigDecimal getRainfall() {return this.rainfall;}

    public void setMonth(Integer month) {
        this.month = month;
    }

    public void setMaxTemp(BigDecimal maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinTemp(BigDecimal minTemp) {
        this.minTemp = minTemp;
    }

    public void setMeanTemp(BigDecimal meanTemp) {
        this.meanTemp = meanTemp;
    }

    public void setMaxTempF(BigDecimal maxTempF) {
        this.maxTempF = maxTempF;
    }

    public void setMinTempF(BigDecimal minTempF) {
        this.minTempF = minTempF;
    }

    public void setMeanTempF(BigDecimal meanTempF) {
        this.meanTempF = meanTempF;
    }

    public void setRaindays(BigDecimal raindays) {
        this.raindays = raindays;
    }

    public void setRainfall(BigDecimal rainfall) {
        this.rainfall = rainfall;
    }
}
