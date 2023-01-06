package org.pulcifer.climate.svc;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.repo.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<SimilarCity> retrieveSimilarCities(Similarity similarity) {
        return new CitySorter(similarity.similarityMultiplier)
                .sortCitiesByClimateDifferences(
                        new SimilarCity(repo.findByCityId(similarity.cityId).get())
                        , repo.findAll(), similarity.minPop, similarity.maxPop);
    }
}

