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
    fun getCityByName(name: String?) = repo!!.findByCityName(name).get()
    fun citiesContaining(cityName: String?) = repo!!.findByCityNameContaining(cityName).get()

    fun retrieveSimilarCities(cityId: Int?, numOfOutliersToRemove: Int?): List<SimilarCity> {
//        return retrieveCitiesByClimateRanges(
//            ClimateRanges(repo!!.findByCityId(cityId).get(), numOfOutliersToRemove), cityId
//        )

        val similarCities = retrieveCitiesSortByClimateDifferences(cityId)
        return similarCities.subList(0,99)
    }

    fun compareCities(cityIds: List<Int>): MutableList<SimilarCity> = repo!!.findByCityIdIn(cityIds).get().stream().map { c -> SimilarCity(c!!) }
            .collect(Collectors.toList())

    private fun retrieveCitiesSortByClimateDifferences(cityId: Int?): List<SimilarCity> {
        val similarCities = repo!!.findAll().stream()
            .filter { filterIncompleteCity(it) }
            .map {SimilarCity(it!!)}
            .collect(Collectors.toList())

        val city = SimilarCity(repo!!.findByCityId(cityId).get())

        val map = mutableMapOf<Int, SimilarCity>()

        similarCities.forEach { map[differenceForCity(city, it)] = it }


        return map.toSortedMap().values.toList()

    }

    private fun differenceForCity(city: SimilarCity, similarCity: SimilarCity): Int {
        var differenceAccumulated = 0
        differenceAccumulated += difference(city.highestAvgTemperature, similarCity.highestTemperature, 1.2F)
        differenceAccumulated += difference(city.lowestAvgTemperature, similarCity.lowestAvgTemperature, 0.9F)
        differenceAccumulated += difference(city.avgTemperature, similarCity.avgTemperature, 1F)
        differenceAccumulated += difference(city.lowestTemperature, similarCity.lowestTemperature, 0.9F)
        differenceAccumulated += difference(city.highestTemperature, similarCity.highestTemperature, 1.2F)
        differenceAccumulated += difference(city.rainDays, similarCity.rainDays, 1.6F)
        differenceAccumulated += difference(city.rainfall, similarCity.rainfall, 0.7F)
        return differenceAccumulated
    }

    private fun difference(cityValue: BigDecimal?, similarCityValue: BigDecimal?, weight: Float?): Int {
        if (cityValue == null || similarCityValue == null) return 0
        val rawDiff = cityValue - similarCityValue
        return abs((rawDiff.toFloat() / cityValue.toFloat()) * 100F * (weight?: 1F)).toInt()
    }

    data class CityDifference(val similarCity: SimilarCity) {
        val difference: Float = 0.0F
    }

    private fun retrieveCitiesByClimateRanges(ranges: ClimateRanges, cityId: Int?): List<SimilarCity> {
        val numOfOutliersToRemove = ranges.numOfOutliersToRemove
        return repo!!.findAll().stream()
            .filter { filterIncompleteCity(it) }
            .map { SimilarCity(it!!) }
            .filter {
                areValuesInRange(
                    it.avgTemperatures,
                    numOfOutliersToRemove,
                    ranges.avgTemp
                )
            }
            .filter {
                areValuesInRange(
                    it.lowestTemperatures,
                    numOfOutliersToRemove,
                    ranges.lowestTemp
                )
            }
            .filter {
                areValuesInRange(
                    it.highestTemperatures,
                    numOfOutliersToRemove,
                    ranges.highestTemp
                )
            }
            .filter { isValueInRange(it.rainDays!!, ranges.rainDays!!) }
            .filter { isValueInRange(it.rainfall!!, ranges.rainfall!!) }
            .collect(Collectors.toList())
    }

    private fun areValuesInRange(values: List<BigDecimal?>?, numOfOutliersToRemove: Int?, range: Range?): Boolean {
        return trimOutliers(values, numOfOutliersToRemove).stream()
            .allMatch { v: BigDecimal? -> isValueInRange(v!!, range!!) }
    }

    private fun trimOutliers(values: List<BigDecimal?>?, numOfOutliersToRemove: Int?): List<BigDecimal?> {
        return if (values!!.size < numOfOutliersToRemove!! * 2) emptyList<BigDecimal>() else values.subList(
            (if (numOfOutliersToRemove == 0) 0 else numOfOutliersToRemove),
            values.size - numOfOutliersToRemove
        )
    }

    private fun isValueInRange(value: BigDecimal, range: Range) = value >= range.min && value <= range.max

    private fun filterIncompleteCity(city: City?) = city!!.climate!!.climateMonth!!.stream()
            .allMatch { filterForCompleteness(it) }

    private fun filterForCompleteness(climateMonth: ClimateMonth?) = climateMonth?.raindays != null && climateMonth.maxTempF != null && climateMonth.minTempF != null && climateMonth.rainfall != null
}