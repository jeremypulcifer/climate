package org.pulcifer.climate.svc;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.svc.diffCalc.DifferenceAggregator;
import org.pulcifer.climate.svc.filters.SimilarCityList;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class CitySorter {

    private final SimilarCityList similarCityList;

    public CitySorter(BigDecimal similarityMultiplier) {
        similarCityList = new SimilarCityList(similarityMultiplier);
    }

    public CitySorter() {
        similarCityList = new SimilarCityList();
    }

    public List<SimilarCity> dontSort(List<City> cities) {
        return similarCityList.findAllCities(cities);
    }
    public List<SimilarCity> sortCitiesByClimateDifferences(SimilarCity city, List<City> cities, Integer minPop, Integer maxPop) {
        city.setDiffScore(0);
        var similarCities = similarCityList.findSimilarCities(city, cities, minPop, maxPop);
        similarCities.forEach(it -> it.setDiffScore((int)new DifferenceAggregator().calcDiff(city, it)));
        return similarCities.stream().sorted(Comparator.comparing(SimilarCity::getDiff))
                .collect(Collectors.toList());
    }
}
