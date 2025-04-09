package org.pulcifer.climate.svc;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.svc.filters.SimilarCityFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CitySorter {

    private final SimilarCityFilter similarCityFilter;

    public CitySorter() {
        similarCityFilter = new SimilarCityFilter();
    }

    public List<SimilarCity> dontSort(List<City> cities) {
        return similarCityFilter.findAllCities(cities);
    }

    public List<SimilarCity> sortCitiesByClimateRankings(SimilarCity city, List<City> cities, Integer minPop, Integer maxPop, Integer returnCount) {
        var similarCities = similarCityFilter.findDemographicallySimilarCities(city, cities, minPop, maxPop, returnCount);

        for (SimilarCity simCity : similarCities) {
            simCity.setHighestTemperatureRank(getIndex(simCity, sortByHighestTemp(city, similarCities)));
            simCity.setLowestTemperatureRank(getIndex(simCity, sortByLowestTemp(city, similarCities)));
            simCity.setHighestAvgTemperatureRank(getIndex(simCity, sortByHighestAvgTemp(city, similarCities)));
            simCity.setLowestAvgTemperatureRank(getIndex(simCity, sortByLowestAvgTemp(city, similarCities)));
            simCity.setAvgTemperatureRank(getIndex(simCity, sortByAvgTemp(city, similarCities)));
            simCity.setRainfallRank(getIndex(simCity, sortByRainfall(city, similarCities)));
            simCity.setRainDaysRank(getIndex(simCity, sortByRainDays(city, similarCities)));
        }

        return similarCities.stream().sorted(Comparator.comparing(SimilarCity::getTotalRank))
                .collect(Collectors.toList());
    }

    private BigDecimal getIndex(SimilarCity city, List<SimilarCity> cities) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getCityId().equals(city.getCityId())) {
                return new BigDecimal(i);
            }
        }
        return BigDecimal.ONE;
    }

    public List<SimilarCity> sortByHighestTemp(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getHighestTemperature().subtract(city.getHighestTemperature()).abs()));
        return cities;
    }
    public List<SimilarCity> sortByLowestTemp(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getLowestTemperature().subtract(city.getLowestTemperature()).abs()));
        return cities;
    }
    public List<SimilarCity> sortByHighestAvgTemp(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getHighestAvgTemperature().subtract(city.getHighestAvgTemperature()).abs()));
        return cities;
    }
    public List<SimilarCity> sortByLowestAvgTemp(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getLowestAvgTemperature().subtract(city.getLowestAvgTemperature()).abs()));
        return cities;
    }
    public List<SimilarCity> sortByAvgTemp(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getAvgTemperature().subtract(city.getAvgTemperature()).abs()));
        return cities;
    }
    public List<SimilarCity> sortByRainfall(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getRainfall().subtract(city.getRainfall()).abs()));
        return cities;
    }
    public List<SimilarCity> sortByRainDays(SimilarCity city, List<SimilarCity> original) {
        List<SimilarCity> cities = new ArrayList<>(original);
        cities.sort(Comparator.comparing(t -> t.getRainDays().subtract(city.getRainDays()).abs()));
        return cities;
    }
}
