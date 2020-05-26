package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClimateMonth {
    Integer month;
    BigDecimal maxTemp;
    BigDecimal minTemp;
    BigDecimal meanTemp;
    BigDecimal maxTempF;
    BigDecimal minTempF;
    BigDecimal meanTempF;
    BigDecimal raindays;
    BigDecimal rainfall;

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

}
