package org.pulcifer.climate.svc;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.ClimateMonth;
import org.pulcifer.climate.range.ClimateRanges;
import org.pulcifer.climate.range.Range;
import org.pulcifer.climate.repo.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    CityRepository repo;

    public Optional<City> getCityById(Integer cityId) {
        return repo.getCityByCityId(cityId);
    }
    public Optional<City> getCityByName(String name) {
        return repo.getCityByCityName(name);
    }
    public Optional<List<City>> citiesContaining(String cityName) { return repo.findByCityNameContaining(cityName); }

    public List<SimilarCity> retrieveSimilarCities(Integer cityId, Integer numOfOutliersToRemove) {
        return retrieveCitiesByClimateRanges(new ClimateRanges(repo.getCityByCityId(cityId).get(), numOfOutliersToRemove));
    }

    private List<SimilarCity> retrieveCitiesByClimateRanges(ClimateRanges ranges) {
        Integer numOfOutliersToRemove = ranges.getNumOfOutliersToRemove();
        return repo.findAll().stream()
                .filter(this::filterIncompleteCity)
                .map(c -> new SimilarCity.Builder().build(c))
                .filter(simCity -> areValuesInRange(simCity.getAvgTemperatures(), numOfOutliersToRemove, ranges.getAvgTemp()))
                .filter(simCity -> areValuesInRange(simCity.getLowestTemperatures(), numOfOutliersToRemove, ranges.getLowestTemp()))
                .filter(simCity -> areValuesInRange(simCity.getHighestTemperatures(), numOfOutliersToRemove, ranges.getHighestTemp()))
                .filter(simCity -> isValueInRange(simCity.getRainDays(), ranges.getRainDays()))
                .filter(simCity -> isValueInRange(simCity.getRainfall(), ranges.getRainfall()))
                .collect(Collectors.toList());
    }

    private boolean areValuesInRange(List<BigDecimal> values, Integer numOfOutliersToRemove, Range range) {
        return trimList(values, numOfOutliersToRemove).stream().allMatch(v -> isValueInRange(v, range));
    }

    private List<BigDecimal> trimList(List<BigDecimal> values, Integer numOfOutliersToRemove) {
        if (values.size() < numOfOutliersToRemove * 2) return Collections.emptyList();
        return values.subList(numOfOutliersToRemove == 0 ? 0 : numOfOutliersToRemove, values.size() - numOfOutliersToRemove);
    }

    private boolean isValueInRange(BigDecimal value, Range range) {
        return value.compareTo(range.getMin()) >= 0 &&
                value.compareTo(range.getMax()) <= 0;
    }

    private boolean filterIncompleteCity(City city) {
        return city.getClimate().getClimateMonth().stream().allMatch(this::filterForCompleteness);
    }

    private boolean filterForCompleteness(ClimateMonth climateMonth) {
        return climateMonth.getRaindays() != null &&
                climateMonth.getMaxTempF() != null &&
                climateMonth.getMinTempF() != null &&
                climateMonth.getRainfall() != null;
    }
}
