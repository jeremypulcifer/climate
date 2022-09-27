package org.pulcifer.climate;

import org.pulcifer.climate.svc.ClimateLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * Simple application to retrieve data from an external source (in this case, WMO)
 * and store in a local database, for use by the climate-service application. Note
 * that executing this will wipe the existing data; don't run this concurrently to
 * the climate-service or you'll get really weird results
 *
 */

@SpringBootApplication
@EnableMongoRepositories
public class ClimateLoaderApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ClimateLoaderApplication.class, args);
	}

	@Autowired
	ClimateLoadService cityService;

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void run(String... args) throws Exception {
		Long countOfStoredRecords = cityService.loadCities();
		log.info("Retrieved {} cities with climate information.", countOfStoredRecords);
	}
}
