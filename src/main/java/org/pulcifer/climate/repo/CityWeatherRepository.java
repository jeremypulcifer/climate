package org.pulcifer.climate.repo;

import org.pulcifer.climate.model.CityWeather;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityWeatherRepository extends MongoRepository<CityWeather, String> {
    CityWeather findBySeedId(Integer seedId);
}
