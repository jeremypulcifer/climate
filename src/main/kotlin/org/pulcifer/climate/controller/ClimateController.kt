package org.pulcifer.climate.controller

import org.pulcifer.climate.model.City
import org.pulcifer.climate.model.SimilarCity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.pulcifer.climate.svc.CityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@RestController
class ClimateController {
    @Autowired
    var svc: CityService? = null
    @GetMapping("/city/{id}")
    fun city(@PathVariable id: Int?): City? {
        return svc!!.getCityById(id)
    }

    @GetMapping("/city")
    fun cityByName(@RequestParam(value = "name") name: String?): List<City?> {
        return svc!!.citiesContaining(name)
    }

//    @GetMapping("/citiesContaining")
//    fun citiesContaining(@RequestParam(value = "name") name: String?): Map<Int?, String> {
//        return svc!!.citiesContaining(name).stream()
//            .collect(Collectors.toMap(
//                Function { obj: City? -> obj.getCityId() }, Function { city: City? -> formatCityCountry(city) })
//            )
//    }

    private fun formatCityCountry(city: City?): String {
        return String.format("%s/%s", city?.cityName, city?.country)
    }

    @GetMapping("/similar")
    fun similar(
        @RequestParam(value = "cityId") cityId: Int?,
        @RequestParam(value = "outliers") outliers: Int
    ): List<SimilarCity?>? {
        return svc!!.retrieveSimilarCities(cityId, outliers)
    }

    @GetMapping("/citiesCompare")
    fun compareCities(@RequestParam(value = "cityId") cityId: Int, @RequestParam(value = "cityIds") cityIds: List<Int>): List<SimilarCity?>? {
        return svc!!.compareCities(cityId, cityIds)
    }
}