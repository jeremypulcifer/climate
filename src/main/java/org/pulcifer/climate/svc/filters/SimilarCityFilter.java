package org.pulcifer.climate.svc.filters;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;

import java.util.List;

import static java.lang.Math.abs;

public class SimilarCityFilter {

    private PopulationFilter populationFilter = new PopulationFilter();
    private IncompleteCityFilter incompleteCityFilter = new IncompleteCityFilter();

    public SimilarCityFilter() {}
    public List<SimilarCity> findDemographicallySimilarCities(SimilarCity city, List<City> cities, Integer minPop, Integer maxPop, Integer returnCount) {
        return cities.stream()
                .filter(incompleteCityFilter::filterIncompleteCity)
                .filter(it -> populationFilter.filterPopulation(it, minPop, maxPop))
                .map(SimilarCity::new)
                .filter(incompleteCityFilter::filterIncompleteSimilarCity)
                .filter(it -> !it.getCityId().equals(city.getCityId()))
                .limit(returnCount)
                .toList();
    }

    public List<SimilarCity> findAllCities(List<City> cities) {
        return cities.stream()
                .map(SimilarCity::new)
                .toList();
    }
}
