package org.pulcifer.climate.repo;

import org.pulcifer.climate.model.CityMeta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityMetaRepository extends MongoRepository<CityMeta, String> {
    CityMeta findByCityId(Integer cityId);
}
