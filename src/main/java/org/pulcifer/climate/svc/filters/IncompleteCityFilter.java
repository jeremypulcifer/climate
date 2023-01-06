package org.pulcifer.climate.svc.filters;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.ClimateMonth;

import java.math.BigDecimal;
import java.util.List;

public class IncompleteCityFilter {
    public boolean filterIncompleteCity(City city) {
        return city != null && filterClimateMonthsForCompleteness(city.getClimate().getClimateMonth());
    }

    private boolean filterClimateMonthsForCompleteness(List<ClimateMonth> climateMonths) {
        return climateMonths != null && !climateMonths.isEmpty() &&
                climateMonths.stream().allMatch(this::filterClimateMonthForCompleteness);
    }

    public boolean filterIncompleteSimilarCity(SimilarCity city) {
        boolean isInvalid =
                city == null ||
                        city.getAvgTemperatures().isEmpty() ||
                        city.getLowestTemperatures().isEmpty() ||
                        city.getHighestTemperatures().isEmpty() ||
                        city.getRainDays().compareTo(BigDecimal.ZERO) < 1 ||
                        city.getRainfall().compareTo(BigDecimal.ZERO) < 1;
        return !isInvalid;
    }

    private boolean filterClimateMonthForCompleteness(ClimateMonth climateMonth) {
        return climateMonth.getRaindays() != null &&
                climateMonth.getMaxTempF() != null &&
                climateMonth.getMinTempF() != null &&
                climateMonth.getRainfall() != null;
    }
}
