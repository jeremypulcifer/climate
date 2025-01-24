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

    BigDecimal hundred = BigDecimal.valueOf(100L);
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
        city.setDiffScore(new BigDecimal(111));
        var similarCities = similarCityList.findSimilarCities(city, cities, minPop, maxPop);
        for (SimilarCity simCity : similarCities) {
            BigDecimal diff = new DifferenceAggregator().calcDiff(city, simCity);
            simCity.setDiffScore(diff);
        }
//        similarCities.forEach(it -> it.setDiffScore(new DifferenceAggregator().calcDiff(city, it).multiply(hundred).intValue()));
        return similarCities.stream().sorted(Comparator.comparing(SimilarCity::getDiff))
                .collect(Collectors.toList());
    }
}
