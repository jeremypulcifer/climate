package org.pulcifer.climate

import org.pulcifer.climate.svc.ClimateLoadService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
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
class ClimateKotlinLoaderApplication: CommandLineRunner {
	@Autowired
	var cityService: ClimateLoadService? = null
	var log: Logger = LoggerFactory.getLogger(javaClass)

	@Throws(Exception::class)
	override fun run(vararg args: String) {
		val countOfStoredRecords = cityService!!.loadCities()
		log.info("Retrieved {} cities with climate information.", countOfStoredRecords)
	}

	fun main(args: Array<String>) {
		SpringApplication.run(ClimateKotlinLoaderApplication::class.java, *args)
	}
}
