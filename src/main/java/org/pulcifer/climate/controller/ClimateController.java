package org.pulcifer.climate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.svc.CityService;
import org.pulcifer.climate.svc.Similarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ClimateController {

    @Autowired
    CityService svc;

    @GetMapping("/city/{id}")
    public City city(@PathVariable Integer id) {
        return svc.getCityById(id);
    }

    @GetMapping("/citiesContaining")
    public Map<Integer, String> citiesContaining(@RequestParam(value = "name") String name) {
        return svc.citiesContaining(name).stream()
                .collect(Collectors.toMap(City::getCityId, this::formatCityCountry));
    }

    private String formatCityCountry(City city) {
        return String.format("%s/%s", city.getCityName(), city.getCountry());
    }

    @GetMapping("/similar/{cityId}")
    public List<SimilarCity> similar(@PathVariable Integer cityId
            , @RequestParam(value = "min_pop") Integer minPop
            , @RequestParam(value = "max_pop") Integer maxPop
            , @RequestParam("returnCount") Integer returnCount) {
        return svc.retrieveSimilarCities(buildSimilarity(cityId, minPop, maxPop, returnCount));
    }

    private Similarity buildSimilarity(Integer cityId
            , Integer minPop
            , Integer maxPop
            , Integer returnCount) {
        Similarity similarity = new Similarity();
        similarity.cityId = cityId;
        similarity.minPop = minPop == null ? 0 : minPop;
        similarity.maxPop = maxPop == null ? 10 ^ 12 : maxPop;
        similarity.returnCount = returnCount == null ? 10 : returnCount;
        return similarity;
    }

    @GetMapping(value = "/allCSV/{cityId}")
    public void similarCSV(@PathVariable Integer cityId
            , @RequestParam(value = "min_pop") Integer minPop
            , @RequestParam(value = "max_pop") Integer maxPop
            , @RequestParam("returnCount") Integer returnCount
            , HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=./climate_similarity.csv");

        try (ServletOutputStream out = response.getOutputStream(); OutputStreamWriter osw = new OutputStreamWriter(out)) {
            CSVPrinter csvPrinter = new CSVPrinter(osw, CSVFormat.DEFAULT
                    .withHeader("country", "city", "id", "population",
                            "avg-temp", "lowest-avg-temp", "highest-avg-temp",
                            "lowest-temp", "highest-temp", "rain days", "rainfall",
                            "rank-avg-temp", "rank-lowest-avg-temp", "rank-highest-avg-temp", "rank-lowest-temp", "rank-highest-temp",
                            "rank-rain-days", "rank-rainfall", "total-rank"));
            List<SimilarCity> cities = similar(cityId, minPop, maxPop, 10000);
            for (SimilarCity city : cities)
                csvPrinter.printRecord(flattenSimilarCity(city));
            osw.flush();
        }
    }

    List<String> flattenSimilarCity(SimilarCity city) {
        List<String> row = new ArrayList<>();
        row.add(city.getCountry());
        row.add(city.getCityName());
        row.add(city.getCityId().toString());
        row.add(city.getPopulation().toString());
        row.add(city.getAvgTemperature().toString());
        row.add(city.getLowestAvgTemperature().toString());
        row.add(city.getHighestAvgTemperature().toString());
        row.add(city.getLowestTemperature().toString());
        row.add(city.getHighestTemperature().toString());
        row.add(city.getRainDays().toString());
        row.add(city.getRainfall().toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.AVG_TEMP).toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.LOWEST_AVG_TEMP).toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.HIGHEST_AVG_TEMP).toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.LOWEST_TEMP).toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.HIGHEST_TEMP).toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.RAIN_DAYS).toString());
        row.add(city.getRanks().get(SimilarCity.ScoreTypes.RAINFALL).toString());
        row.add(city.getTotalRank().toString());

        return row;
    }

    /*
    {
        "ranks": {
            "LOWEST_AVG_TEMP": 17,
            "LOWEST_TEMP": 42,
            "AVG_TEMP": 31,
            "RAINFALL": 6,
            "HIGHEST_AVG_TEMP": 88,
            "HIGHEST_TEMP": 61,
            "RAIN_DAYS": 3
        },
        "country": "Spain",
        "cityName": "Barcelona",
        "population": 1650457,
        "highestAvgTemperature": 75.85,
        "lowestAvgTemperature": 48.5,
        "avgTemperature": 60.92,
        "lowestTemperature": 40.5,
        "highestTemperature": 83.3,
        "rainDays": 72.5,
        "rainfall": 581.5,
        "totalRank": 249,
        "cityId": 1232
     */

    @GetMapping(value = "/allJSON/")
    public void allJSON()
            throws IOException {
        ObjectWriter mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
        for (SimilarCity city : svc.retrieveAllCities()) {
            mapper.writeValue(new File("/Users/jpulcifer/personal/climate/data-json/"+city.getCityId()+".json"), city);
        }
    }

    @GetMapping(value = "/similarCSV/{cityId}")
    public void similarCSV(@PathVariable Integer cityId
            , @RequestParam(value = "min_pop") Integer minPop
            , @RequestParam(value = "max_pop") Integer maxPop
            , @RequestParam(value = "similarityMultiplier") BigDecimal similarityMultiplier
            , HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=myFile.csv");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println("country,city,id,highest avg temp,lowest avg temp,avg-temp,lowest-temp,highest-temp,rain days,rainfall,diff");
            List<SimilarCity> cities = svc.retrieveSimilarCities(buildSimilarity(cityId, minPop, maxPop, 10000));
            for (SimilarCity city : cities) out.println(cityDataAsCSV(city));
            out.flush();
        }
    }

    @GetMapping(value = "/cities/csv")
    public void similarCSV(HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=cities.csv");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println("id,country,city,lat,long,pop");
            List<SimilarCity> cities = svc.retrieveAllCities();
            for (SimilarCity city : cities) out.println(cityLocationDataAsCSV(city));
            out.flush();
        }
    }

    private String cityDataAsCSV(SimilarCity c) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                c.getCountry().replaceAll(",", "-"), c.getCityName().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getPopulation(),
                c.getCityId(),
                c.getHighestAvgTemperature(),
                c.getLowestAvgTemperature(),
                c.getAvgTemperature(),
                c.getLowestTemperature(),
                c.getHighestTemperature(),
                c.getRainDays(), c.getRainfall(), c.getTotalRank());
    }
    private String citySimpleDataAsCSV(SimilarCity c) {
        return String.format("%s,%s,%s,%s,%s",
                c.getCountry().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getCityName().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getPopulation(),
                c.getCityId(),
                c.getAvgTemperature());
    }
    private String cityLocationDataAsCSV(SimilarCity c) {
        return String.format("%s,%s,%s,%s,%s,%s",c.getCityId(),
                c.getCountry().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getCityName().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getLatitude(),
                c.getLongitude(),
                c.getPopulation());
    }
}
