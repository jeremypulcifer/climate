package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Climate {
    private String raintype;
    private String raindef;
    private String rainunit;
    private BigDecimal datab;
    private BigDecimal datae;
    private BigDecimal tempb;
    private BigDecimal tempe;
    private BigDecimal rdayb;
    private BigDecimal rdaye;
    private BigDecimal rainfallb;
    private BigDecimal rainfalle;
    private String climatefromclino;
    private List<ClimateMonth> climateMonth;

    public String getRaintype() {return this.raintype;}
    public String getRaindef() {return this.raindef;}
    public String getRainunit() {return this.rainunit;}
    public BigDecimal getDatab() {return this.datab;}
    public BigDecimal getDatae() {return this.datae;}
    public BigDecimal getTempb() {return this.tempb;}
    public BigDecimal getTempe() {return this.tempe;}
    public BigDecimal getRdayb() {return this.rdayb;}
    public BigDecimal getRdaye() {return this.rdaye;}
    public BigDecimal getRainfallb() {return this.rainfallb;}
    public BigDecimal getRainfalle() {return this.rainfalle;}
    public String getClimatefromclino() {return this.climatefromclino;}
    public List<ClimateMonth> getClimateMonth() {return this.climateMonth;}

    public void setClimateMonth(List<ClimateMonth> climateMonth) {
        this.climateMonth = climateMonth;
    }
}
