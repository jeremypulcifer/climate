package org.pulcifer.climate.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Climate {
    String raintype;
    String raindef;
    String rainunit;
    BigDecimal datab;
    BigDecimal datae;
    BigDecimal tempb;
    BigDecimal tempe;
    BigDecimal rdayb;
    BigDecimal rdaye;
    BigDecimal rainfallb;
    BigDecimal rainfalle;
    String climatefromclino;
    List<ClimateMonth> climateMonth;

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
}
