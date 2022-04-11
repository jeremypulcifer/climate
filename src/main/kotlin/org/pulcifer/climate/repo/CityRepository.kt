package org.pulcifer.climate.repo

import org.pulcifer.climate.model.City
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface CityRepository : MongoRepository<City?, String?> {
    fun findByCityId(cityId: Int?): Optional<City?>
    fun findByCityIdNot(cityId: Int?): Optional<List<City?>>
    fun findByCityIdIn(cityIds: List<Int?>?): Optional<List<City?>>
    fun findByCityName(cityName: String?): Optional<City?>
    fun findByCityNameContaining(cityName: String?): Optional<List<City?>?>
}