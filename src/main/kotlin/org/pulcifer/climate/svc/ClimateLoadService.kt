package org.pulcifer.climate.svc

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.pulcifer.climate.model.City
import org.pulcifer.climate.model.wrapper.CityWrapper
import org.pulcifer.climate.repo.CityRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.StringReader
import java.util.concurrent.*
import java.util.function.Consumer
import java.util.function.Supplier

/**
 *
 * Simple grab-and-store. There's little (okay, none) testing
 * and only minor performance enhancements, as this is intended
 * to be a once-ever execution, or at most a monthly action.
 *
 * Mongodb is used as the local storage. It would be trivial
 * to move to a relational db, but I can't imagine a compelling
 * reason to do so.
 *
 * Kudos and Acknowledgement must be given to the
 * WMO World Weather Information Service
 * (https://worldweather.wmo.int) as the source of information.
 * WMO Disclaimer: http://worldweather.wmo.int/en/disclaimer.html
 *
 */
@Service
class ClimateLoadService {
    @Autowired
    var repo: CityRepository? = null
    var log = LoggerFactory.getLogger(javaClass)
    var citiesUrl = "https://worldweather.wmo.int/en/json/%s_en.json"
    var cityListUrl = "https://worldweather.wmo.int/en/json/full_city_list.txt"
    fun loadCities(): Long {
        // only execute if there are more than a smaller test set
        val existingCount: Long = repo!!.count()
        if (existingCount > 100) return existingCount
        val cityFile = RestTemplate().getForObject<String>(cityListUrl, String::class.java)
        val records: Iterable<CSVRecord> = CSVFormat.DEFAULT
            .withHeader("Country", "City", "CityId")
            .withFirstRecordAsHeader().withDelimiter(';').withQuote('\"')
            .parse(StringReader(cityFile))
        val executorService: ExecutorService = ThreadPoolExecutor(
            10, 50, 100L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue<Runnable>()
        )
        val cities: MutableSet<City> = HashSet()
        records.forEach(Consumer<CSVRecord> { r: CSVRecord ->
            CompletableFuture.supplyAsync<City>(
                Supplier<City?> { parseFromCsvRecord(r) }, executorService
            )
                .thenAccept(Consumer<City> { e: City -> cities.add(e) })
        })
        executorService.shutdown()
        // ok, this is lazy, but as the run takes a bit of time
        // I've not made it a priority to clean this up
        executorService.awaitTermination(5L, TimeUnit.HOURS)
        // due to the limitations of the apache csv library, I need
        // to capture the exception case, as indicated by a city with
        // a cityId == 0, and discard.
        cities.remove(falseCity)
        repo!!.saveAll<City>(cities)
        return repo!!.count()
    }

    private val falseCity: City = City(0)
    private fun parseFromCsvRecord(csvRecord: CSVRecord): City? {
        // there's a footer line to ignore. Could configure the CSV parser, but this is easier
        if (csvRecord.size() < 3) {
            log.info("Invalid CSVRecord:$csvRecord")
            return falseCity
        }
        log.info("Getting climate data for id {}, {}/{}", csvRecord.get(2), csvRecord.get(0), csvRecord.get(1))
        val city: City? = getCity(Integer.valueOf(csvRecord.get(2)))
        city!!.country = csvRecord.get(0)
        return city
    }

    private fun getCity(cityId: Int): City? {
        return try {
            RestTemplate().getForObject<CityWrapper>(String.format(citiesUrl, cityId), CityWrapper::class.java)?.city
        } catch (t: Throwable) {
            log.error("Couldn't create City object from input for city id {}", cityId, t)
            val goofy =
                RestTemplate().getForObject<String>(String.format(citiesUrl, cityId), String::class.java)
            log.error("goofy record: ", goofy)
            null
        }
    }
}