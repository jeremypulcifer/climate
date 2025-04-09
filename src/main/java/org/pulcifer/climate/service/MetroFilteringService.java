package org.pulcifer.climate.service;

import org.pulcifer.climate.calculators.DistanceCalculator;
import org.pulcifer.climate.model.CityMeta;
import org.pulcifer.climate.repo.CityMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MetroFilteringService {

    private CityMetaRepository repo;
    @Autowired
    public void setCityMetaRepository(CityMetaRepository repo) { this.repo = repo; }

    private static final int range = 50;

    public List<CityMeta> identifyMetroAreas() {
        List<CityMeta> cities = repo.findAll();
        cities.sort(Comparator.comparing(CityMeta::getPopulation).reversed());

        // reset list of cities; all are set to primary as we will set
        // secondary cities from list
        for (CityMeta city : cities) {
            city.setPrimary(true);
        }


        int i = 0;
        for (CityMeta city : cities) {
            i++;
            if (i >= cities.size()) break;
            // prior loops may have made this city non-primary, skip in this case
            if (!city.isPrimary()) {
                continue;
            }
            cities.subList(i, cities.size() - 1);
            for (CityMeta secondaryCity : cities.subList(i, cities.size() - 1)) {
                Integer miles = DistanceCalculator.calculateDistanceBetweenCities(city, secondaryCity);
                if (miles < range) {
                    secondaryCity.setPrimary(false);
//                    secondaryCity.setPrimaryCity(city);
                    city.getSecondaryCities().add(secondaryCity);
                }
            }
        }

//        List<CityMeta> primariesOnly = cities.stream().filter(CityMeta::isPrimary).toList();
        return cities;









    }
}
