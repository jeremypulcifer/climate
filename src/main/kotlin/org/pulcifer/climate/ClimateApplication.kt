package org.pulcifer.climate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.beans.factory.annotation.Autowired
import org.pulcifer.climate.svc.ClimateLoadService
import kotlin.Throws
import kotlin.jvm.JvmStatic
import org.springframework.boot.SpringApplication
import org.pulcifer.climate.ClimateApplication
import org.slf4j.LoggerFactory
import java.lang.Exception

/**
 *
 * Simple application to retrieve data from an external source (in this case, WMO)
 * and store in a local database, for use by the climate-service application. Note
 * that executing this will wipe the existing data; don't run this concurrently to
 * the climate-service or you'll get really weird results
 *
 */
//@SpringBootApplication
class ClimateApplication : CommandLineRunner {
    @Autowired
    var cityService: ClimateLoadService? = null
    var log = LoggerFactory.getLogger(javaClass)
    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val countOfStoredRecords = cityService!!.loadCities()
        log.info("Retrieved {} cities with climate information.", countOfStoredRecords)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ClimateApplication::class.java, *args)
        }
    }
}