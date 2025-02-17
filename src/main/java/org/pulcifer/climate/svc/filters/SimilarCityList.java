package org.pulcifer.climate.svc.filters;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class SimilarCityList {

    private SimilarClimateFilter similarClimateFilter;
    private PopulationFilter populationFilter;
    private IncompleteCityFilter incompleteCityFilter;

    public SimilarCityList(BigDecimal similarityMultiplier) {
        similarClimateFilter = new SimilarClimateFilter(similarityMultiplier);
        populationFilter = new PopulationFilter();
        incompleteCityFilter = new IncompleteCityFilter();
    }
    public SimilarCityList() {}
    public List<SimilarCity> findSimilarCities(SimilarCity city, List<City> cities, Integer minPop, Integer maxPop) {
        List<SimilarCity> simCities = cities.stream()
                .filter(incompleteCityFilter::filterIncompleteCity)
                .filter(it -> populationFilter.filterPopulation(it, minPop, maxPop))
                .map(SimilarCity::new)
                .filter(incompleteCityFilter::filterIncompleteSimilarCity)
                .filter(it -> similarClimateFilter.filterOnlySimilarClimates(it, city))
//                .filter(it -> !it.getCityId().equals(city.getCityId()))
                .collect(Collectors.toList());
        simCities.add(city);
        return simCities;
    }

    public List<SimilarCity> findAllCities(List<City> cities) {
        return cities.stream()
                .map(SimilarCity::new)
                .collect(Collectors.toList());
    }
}
