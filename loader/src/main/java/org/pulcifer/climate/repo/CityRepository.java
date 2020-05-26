package org.pulcifer.climate.repo;

import org.pulcifer.climate.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {
}
