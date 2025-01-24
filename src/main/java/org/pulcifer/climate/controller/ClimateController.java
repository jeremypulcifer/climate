package org.pulcifer.climate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/city_with_temperature_range")
    public City setTemperatureRange(@RequestParam(value = "city_id") Integer cityId) {
        return svc.setTemperatureRange(cityId);
    }

    @GetMapping("/similar/{cityId}")
    public List<SimilarCity> similar(@PathVariable Integer cityId
            , @RequestParam(value = "min_pop") Integer minPop
            , @RequestParam(value = "max_pop") Integer maxPop
            , @RequestParam(value = "similarityMultiplier") BigDecimal similarityMultiplier) {
        return svc.retrieveSimilarCities(buildSimilarity(cityId, minPop, maxPop, similarityMultiplier));
    }

    private Similarity buildSimilarity(Integer cityId
            , Integer minPop
            , Integer maxPop
            , BigDecimal similarityMultiplier) {
        Similarity similarity = new Similarity();
        similarity.cityId = cityId;
        similarity.minPop = minPop == null ? 0 : minPop;
        similarity.maxPop = maxPop == null ? 10 ^ 12 : maxPop;
        similarity.similarityMultiplier = similarityMultiplier == null ? BigDecimal.ONE : similarityMultiplier;
        return similarity;
    }

    @GetMapping(value = "/allCSV/")
    public void allCSV()
            throws IOException {
        try (FileWriter fileWriter = new FileWriter("/Users/jpulcifer/personal/climate/data-in/in.csv");
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("country,city,population,id,avg-temp");
            for (SimilarCity city : svc.retrieveAllCities()) printWriter.println(citySimpleDataAsCSV(city));
        }
    }

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
        response.setHeader("Content-Disposition", "attachment;filename=myFile.txt");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println("country,city,population,id,avg-temp,lowest-temp,highest-temp,rain days,rainfall,diff");
            List<SimilarCity> cities = svc.retrieveSimilarCities(buildSimilarity(cityId, minPop, maxPop, similarityMultiplier));
            for (SimilarCity city : cities) out.println(cityDataAsCSV(city));
            out.flush();
        }
    }

    private String cityDataAsCSV(SimilarCity c) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                c.getCountry().replaceAll(",", "-"),
                c.getCityName().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getPopulation(),
                c.getCityId(),
//                c.getHighestAvgTemperature(),
//                c.getLowestAvgTemperature(),
                c.getAvgTemperature(),
                c.getLowestTemperature(),
                c.getHighestTemperature(),
                c.getRainDays(),
                c.getRainfall(),
                c.getDiff());
    }
    private String citySimpleDataAsCSV(SimilarCity c) {
        return String.format("%s,%s,%s,%s,%s",
                c.getCountry().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getCityName().replaceAll(",", "-").replaceAll("- ", "-"),
                c.getPopulation(),
                c.getCityId(),
                c.getAvgTemperature());
    }
}
