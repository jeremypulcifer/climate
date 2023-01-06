package org.pulcifer.climate.svc.filters;

import org.pulcifer.climate.model.City;

public class PopulationFilter {

    public boolean filterPopulation(City city, Integer minPop, Integer maxPop) {
        if (minPop != null)
            if (city.getPopulation() == null || city.getPopulation() < minPop) return false;
        if (maxPop != null)
            return city.getPopulation() != null && city.getPopulation() <= maxPop;
        return true;
    }

}
