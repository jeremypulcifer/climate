package org.pulcifer.climate.svc

import org.pulcifer.climate.model.SimilarCity
import org.pulcifer.climate.model.City
import org.pulcifer.climate.model.ClimateMonth
import org.pulcifer.climate.range.ClimateRanges
import org.pulcifer.climate.range.Range
import org.pulcifer.climate.repo.CityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors
import kotlin.math.abs

@Service
class CityService {
    @Autowired
    var repo: CityRepository? = null

    fun getCityById(cityId: Int?) = repo!!.findByCityId(cityId).get()
    fun getCityByName(cityName: String?) = repo!!.findByCityNameContaining(cityName).get()
//    fun getCityByName(cityName: String?) = repo!!.findByCityName(cityName).get()
    fun citiesContaining(cityName: String?) = repo!!.findByCityNameContaining(cityName).get()

    fun retrieveSimilarCities(cityId: Int?, numOfOutliersToRemove: Int?): List<SimilarCity> {
//        return retrieveCitiesByClimateRanges(
//            ClimateRanges(repo!!.findByCityId(cityId).get(), numOfOutliersToRemove), cityId
//        )

        val similarCities = retrieveCitiesSortByClimateDifferences(cityId)
        return similarCities.subList(0,99)
    }

    fun citiesByCountryNameContains(name: String?) = repo!!.findByCountryContaining(name).get()

    fun compareCities(cityId: Int, cityIds: List<Int>): List<SimilarCity> =
        sortCitiesByClimateDifferences(cityId, repo!!.findByCityIdIn(cityIds + cityId).get())

    private fun retrieveCitiesSortByClimateDifferences(cityId: Int?): List<SimilarCity> {
        return sortCitiesByClimateDifferences(cityId, repo!!.findAll())
    }

    private fun sortCitiesByClimateDifferences(cityId: Int?, cities: List<City?>): List<SimilarCity> {
        val city = SimilarCity(repo!!.findByCityId(cityId).get(), 0)
        val similarCities = cities.stream()
            .filter { filterIncompleteCity(it) }
            .map {SimilarCity(it!!, differenceForCityV2(city, SimilarCity(it, 0)))}
            .collect(Collectors.toList())
        similarCities.sortBy { it.diff }
        similarCities.forEach { entry ->
            println("${entry.diff} : ${entry.cityName}")
        }
        return similarCities

//        val map = mutableMapOf<Int, SimilarCity>()
//        similarCities.forEach { map[differenceForCityV2(city, it)] = it }
//        for (i in similarCities.indices) {
//            val diffScore = differenceForCityV2(city, similarCities[i])
//            println("${similarCities[i].cityName} $diffScore")
//            map[diffScore] = similarCities[i]
//        }

//        val sorted = map.toSortedMap(compareBy<Int> { it })
//
//
//        val sortedMap = map.toSortedMap()
//        sortedMap.forEach { entry ->
//            println("${entry.key} : ${entry.value.cityName}")
//        }
//
//        return sortedMap.values.toList()
    }

//    private fun differenceForCity(city: SimilarCity, similarCity: SimilarCity): Int {
//        var differenceAccumulated = 0
//        differenceAccumulated += difference(city.highestAvgTemperature, similarCity.highestAvgTemperature, 1.2F)
//        differenceAccumulated += difference(city.lowestAvgTemperature, similarCity.lowestAvgTemperature, 0.9F)
////        differenceAccumulated += difference(city.avgTemperature, similarCity.avgTemperature, 1F)
////        differenceAccumulated += difference(city.lowestTemperature, similarCity.lowestTemperature, 0.9F)
////        differenceAccumulated += difference(city.highestTemperature, similarCity.highestTemperature, 1.2F)
//        differenceAccumulated += difference(city.rainDays, similarCity.rainDays, 1.6F)
////        differenceAccumulated += difference(city.rainfall, similarCity.rainfall, 0.7F)
//        return differenceAccumulated
//    }
//
//    private fun difference(cityValue: BigDecimal?, similarCityValue: BigDecimal?, weight: Float?): Int {
//        if (cityValue == null || similarCityValue == null) return 0
//        val rawDiff = cityValue - similarCityValue
//        return abs((rawDiff.toFloat() / cityValue.toFloat()) * 100F * (weight?: 1F)).toInt()
//    }


    private fun differenceForCityV2(city: SimilarCity, similarCity: SimilarCity): Int {
        var differenceAccumulated = 1000000F
        var diffHighTemp = differenceV2(city.highestAvgTemperature, similarCity.highestAvgTemperature)
        if (diffHighTemp > 0) differenceAccumulated *= diffHighTemp
        var diffLowTemp = differenceV2(city.lowestAvgTemperature, similarCity.lowestAvgTemperature)
        if (diffLowTemp > 0) differenceAccumulated *= diffLowTemp
//        differenceAccumulated *= differenceV2(city.avgTemperature, similarCity.avgTemperature)
//        differenceAccumulated *= differenceV2(city.lowestTemperature, similarCity.lowestTemperature)
//        differenceAccumulated *= differenceV2(city.highestTemperature, similarCity.highestTemperature)
        var diffRainDays = differenceV2(city.rainDays, similarCity.rainDays)
        if (diffRainDays > 0) differenceAccumulated *= diffRainDays
//        differenceAccumulated *= differenceV2(city.rainfall, similarCity.rainfall)
        return differenceAccumulated.toInt()
    }

    private fun differenceV2(cityValue: BigDecimal?, similarCityValue: BigDecimal?): Float {
        if (cityValue == null || similarCityValue == null) return 1F
        var diff = abs(cityValue.toFloat() - similarCityValue.toFloat())
        if (diff == 0F) diff = 1F
        val r = diff / cityValue.toFloat()
        return if (r == 0F) 1F
        else r
    }

//    private fun retrieveCitiesByClimateRanges(ranges: ClimateRanges, cityId: Int?): List<SimilarCity> {
//        val numOfOutliersToRemove = ranges.numOfOutliersToRemove
//        return repo!!.findAll().stream()
//            .filter { filterIncompleteCity(it) }
//            .map { SimilarCity(it!!) }
//            .filter {
//                areValuesInRange(
//                    it.avgTemperatures,
//                    numOfOutliersToRemove,
//                    ranges.avgTemp
//                )
//            }
//            .filter {
//                areValuesInRange(
//                    it.lowestTemperatures,
//                    numOfOutliersToRemove,
//                    ranges.lowestTemp
//                )
//            }
//            .filter {
//                areValuesInRange(
//                    it.highestTemperatures,
//                    numOfOutliersToRemove,
//                    ranges.highestTemp
//                )
//            }
//            .filter { isValueInRange(it.rainDays!!, ranges.rainDays!!) }
//            .filter { isValueInRange(it.rainfall!!, ranges.rainfall!!) }
//            .collect(Collectors.toList())
//    }
//
//    private fun areValuesInRange(values: List<BigDecimal?>?, numOfOutliersToRemove: Int?, range: Range?): Boolean {
//        return trimOutliers(values, numOfOutliersToRemove).stream()
//            .allMatch { v: BigDecimal? -> isValueInRange(v!!, range!!) }
//    }
//
//    private fun trimOutliers(values: List<BigDecimal?>?, numOfOutliersToRemove: Int?): List<BigDecimal?> {
//        return if (values!!.size < numOfOutliersToRemove!! * 2) emptyList<BigDecimal>() else values.subList(
//            (if (numOfOutliersToRemove == 0) 0 else numOfOutliersToRemove),
//            values.size - numOfOutliersToRemove
//        )
//    }

    private fun isValueInRange(value: BigDecimal, range: Range) = value >= range.min && value <= range.max

    private fun filterIncompleteCity(city: City?) = city!!.climate!!.climateMonth!!.stream()
            .allMatch { filterForCompleteness(it) }

    private fun filterForCompleteness(climateMonth: ClimateMonth?) = climateMonth?.raindays != null && climateMonth.maxTempF != null && climateMonth.minTempF != null && climateMonth.rainfall != null
}