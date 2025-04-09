package org.pulcifer.climate.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CityMeta {
    private String city;
    private String country;
    private String latitude;
    private String longitude;
    private Integer population;
    private Integer cityId;
    private Map<ScoreCategories, ScoreAndRank> scores = new HashMap<>();
    private boolean primary;
//    private CityMeta primaryCity;
    private List<CityMeta> secondaryCities = new ArrayList<>();

    public CityMeta() {}

    public CityMeta(String str) {
        String[] words = str.split(",");
//        Geoname ID,City,Country,Population,Timezone,Lat,Long
        cityId = Integer.parseInt(words[0]);
        city = words[1];
        country = words[2];
        population = Integer.parseInt(words[3]);
        latitude = words[5];
        longitude = words[6];
    }

    public enum ScoreCategories {
        TOTAL, TEMPERATURE, HUMIDITY, RAIN, CLOUD_COVER;
    }

    public static class ScoreAndRank {
    public ScoreAndRank(BigDecimal score) {this.score = score; }
        public BigDecimal score;
        public Integer rank;
    }
}
