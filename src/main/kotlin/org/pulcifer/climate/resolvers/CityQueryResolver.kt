package org.pulcifer.climate.resolvers

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.pulcifer.climate.model.SimilarCity
import org.pulcifer.climate.model.City
import org.pulcifer.climate.svc.CityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CityQueryResolver : GraphQLQueryResolver {

    @Autowired
    var svc: CityService? = null

    fun city(id: Int?): City? {
        return svc!!.getCityById(id)
    }

    fun cityByName(name: String?): List<City>? {
        return svc!!.getCityByName(name)
    }

    fun cityByNameContains(name: String?): List<City?> {
        return svc!!.citiesContaining(name)
    }

    fun cityByCountryNameContains(name: String?): List<City?> {
        return svc!!.citiesByCountryNameContains(name)
    }

    fun similar(cityId: Int, outliers: Int? = 0): List<SimilarCity>? {
        return svc!!.retrieveSimilarCities(cityId, outliers)
    }

    fun compareCities(cityId: Int, cityIds: List<Int>): List<SimilarCity>? {
        return svc!!.compareCities(cityId, cityIds)
    }
}
