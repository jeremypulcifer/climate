package org.pulcifer.climate.svc;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.repo.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    CityRepository repo;

    @Autowired
    public void setRepo(CityRepository repo) {
        this.repo = repo;
    }

    public City getCityById(Integer cityId) {
        return repo.findByCityId(cityId).get();
    }
    public List<City> citiesContaining(String cityName) { return repo.findByCityNameContaining(cityName).get(); }

    public List<SimilarCity> retrieveAllCities() {
        return new CitySorter().dontSort(repo.findAll());
    }
    public List<SimilarCity> retrieveSimilarCities(Similarity similarity) {
        return new CitySorter()
                .sortCitiesByClimateRankings(
                        new SimilarCity(repo.findByCityId(similarity.cityId).get())
                        , repo.findAll(), similarity.minPop, similarity.maxPop, similarity.returnCount);
    }
}
