package org.pulcifer.climate.repo;

import org.pulcifer.climate.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends MongoRepository<City, String> {
    Optional<City> getCityByCityId(Integer cityId);
    Optional<City> getCityByCityName(String cityName);
    Optional<List<City>> findByCityNameContaining(String cityName);
}
