package org.pulcifer.climate.svc;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.ClimateMonth;
import org.pulcifer.climate.repo.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
public class CityService {

    @Autowired
    CityRepository repo;

    public City getCityById(Integer cityId) {
        return repo.findByCityId(cityId).get();
    }
    public List<City> citiesContaining(String cityName) { return repo.findByCityNameContaining(cityName).get(); }

    public List<SimilarCity> retrieveSimilarCities(Integer cityId, Integer minPop, Integer maxPop) {
        return sortCitiesByClimateDifferences(cityId, repo.findAll(), minPop, maxPop);
    }
    public List<City> retrieveCities() {
        return repo.findAll();
    }

    private List<SimilarCity> sortCitiesByClimateDifferences(Integer cityId, List<City> cities, Integer minPop, Integer maxPop) {
        var city = new SimilarCity(repo.findByCityId(cityId).get());
        city.setDiffScore(0);
        List<SimilarCity> similarCities = cities.stream()
                .filter(this::filterIncompleteCity)
                .filter(it -> filterPopulation(it, minPop, maxPop))
                .map(SimilarCity::new)
                .filter(this::filterIncompleteSimilarCity)
                .filter(it -> filterSimilarClimate(it, city))
                .collect(Collectors.toList());

        similarCities.forEach(it -> it.setDiffScore(differenceForCityV2(city, it)));
        return similarCities.stream().sorted(Comparator.comparingInt(SimilarCity::getDiff))
                .collect(Collectors.toList());
    }

    private boolean filterPopulation(City city, Integer minPop, Integer maxPop) {
        if (minPop != null)
            if (city.getPopulation() == null || city.getPopulation() < minPop) return false;
        if (maxPop != null)
            if (city.getPopulation() == null || city.getPopulation() > maxPop) return false;
        return true;
    }

    private Integer differenceForCityV2(SimilarCity city, SimilarCity similarCity) {
        float differenceAccumulated = 10000F;
        float diffHighTemp = differenceV2(city.getHighestAvgTemperature(), similarCity.getHighestAvgTemperature());
        if (diffHighTemp > 0) differenceAccumulated *= diffHighTemp;
        float diffLowTemp = differenceV2(city.getLowestAvgTemperature(), similarCity.getLowestAvgTemperature());
        if (diffLowTemp > 0) differenceAccumulated *= diffLowTemp;
        differenceAccumulated *= differenceV2(city.getAvgTemperature(), similarCity.getAvgTemperature());
        differenceAccumulated *= differenceV2(city.getLowestTemperature(), similarCity.getLowestTemperature());
        differenceAccumulated *= differenceV2(city.getHighestTemperature(), similarCity.getHighestTemperature());
        float diffRainDays = differenceV2(city.getRainDays(), similarCity.getRainDays());
        if (diffRainDays > 0) differenceAccumulated *= diffRainDays;
        differenceAccumulated *= differenceV2(city.getRainfall(), similarCity.getRainfall());
        return (int)(differenceAccumulated * -1);
    }

    private Float differenceV2(BigDecimal cityValue, BigDecimal similarCityValue) {
        if (cityValue == null || similarCityValue == null) return 1F;
        var diff = cityValue.floatValue() - abs(cityValue.floatValue() - similarCityValue.floatValue());
        if (diff == 0F) return 0F;
        var r = diff / cityValue.floatValue();
        if (r == 0F) return 0F;
        return r;
    }

    private boolean filterIncompleteCity(City city) {
        return city != null &&
                city.getClimate().getClimateMonth().stream().allMatch(this::filterForCompleteness);
    }
    private boolean filterIncompleteSimilarCity(SimilarCity city) {
        boolean isInvalid =
                city == null ||
                        city.getAvgTemperatures().isEmpty() ||
                        city.getLowestTemperatures().isEmpty() ||
                        city.getHighestTemperatures().isEmpty() ||
                        city.getRainDays().compareTo(BigDecimal.ZERO) < 1 ||
                        city.getRainfall().compareTo(BigDecimal.ZERO) < 1;
        return !isInvalid;
    }

    private boolean filterSimilarClimate(SimilarCity maybeSimilar, SimilarCity city) {
        BigDecimal minLowestTemperature = city.getLowestTemperature().divide(new BigDecimal("1.3"), RoundingMode.HALF_UP);
        BigDecimal maxHighestTemperature = city.getHighestTemperature().multiply(new BigDecimal("1.3"));
        return maybeSimilar.getLowestTemperature().compareTo(minLowestTemperature) > 0 && maybeSimilar.getHighestTemperature().compareTo(maxHighestTemperature) < 0;
    }

    private boolean filterForCompleteness(ClimateMonth climateMonth) {
        return climateMonth.getRaindays() != null &&
                climateMonth.getMaxTempF() != null &&
                climateMonth.getMinTempF() != null &&
                climateMonth.getRainfall() != null;
    }
}
