package org.pulcifer.climate.controller;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.svc.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/city")
    public City cityByName(@RequestParam(value = "name") String name) {
        return svc.getCityByName(name);
    }

    @GetMapping("/citiesContaining")
    public Map<Integer, String> citiesContaining(@RequestParam(value = "name") String name) {
       return svc.citiesContaining(name).stream()
                .collect(Collectors.toMap(City::getCityId, this::formatCityCountry));
    }

    private String formatCityCountry(City city) {
        return String.format("%s/%s", city.getCityName(), city.getCountry());
    }

    @GetMapping("/similar")
    public List<SimilarCity> similar(@RequestParam(value = "cityId")Integer cityId,
                                     @RequestParam(value = "outliers")Integer outliers) {
        return svc.retrieveSimilarCities(cityId, outliers);
    }

    @GetMapping("/citiesCompare")
    public List<SimilarCity> compareCities(@RequestParam(value = "cityId")List<Integer> cityIds) {
        return svc.compareCities(cityIds);
    }
}
