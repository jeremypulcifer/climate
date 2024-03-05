package org.pulcifer.climate.svc;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.model.wrapper.CityWrapper;
import org.pulcifer.climate.repo.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * Simple grab-and-store. There's little (okay, none) testing
 * and only minor performance enhancements, as this is intended
 * to be a once-ever execution, or at most a monthly action.
 * Mongodb is used as the local storage. It would be trivial
 * to move to a relational db, but I can't imagine a compelling
 * reason to do so.
 * Kudos and Acknowledgement must be given to the
 * WMO World Weather Information Service
 * (<a href="https://worldweather.wmo.int">...</a>) as the source of information.
 * WMO Disclaimer: <a href="http://worldweather.wmo.int/en/disclaimer.html">...</a>
 *
 */
@Service
public class ClimateLoadService {

    @Autowired
    CityRepository repo;

    Logger log = LoggerFactory.getLogger(getClass());

    String citiesUrl = "https://worldweather.wmo.int/en/json/%s_en.json";
    String cityListUrl = "https://worldweather.wmo.int/en/json/full_city_list.txt";

    Map<Integer, Integer> populations = new HashMap<>();

    private void loadPopulations() {
        repo.findAll();
        String[] HEADERS = {"id", "population"};
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cities_cities.csv").getFile());

        try (Reader in = new FileReader(file)) {
            Iterable<CSVRecord> records = CSVFormat.Builder.create()
                    .setHeader(HEADERS).setSkipHeaderRecord(true)
                    .build().parse(in);
            for (CSVRecord record : records) {
                Integer id = Integer.parseInt(record.get("id"));
                Integer population = Integer.parseInt(record.get("population"));
                populations.put(id, population);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Long loadCities() throws IOException, InterruptedException {
        // only execute if there are more than a smaller test set
        long existingCount = repo.count();
        if (existingCount > 1000) return existingCount;

        repo.deleteAll();

        loadPopulations();

        String cityFile = new RestTemplate().getForObject(cityListUrl, String.class);

        log.info("cityFile: {}", cityFile);

        String[] HEADERS = {"Country", "City", "CityId"};

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader().withDelimiter(';').withQuote('\"')
                .parse(new StringReader(cityFile));

        ExecutorService executorService =
                new ThreadPoolExecutor(10, 50, 100L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());
        Set<City> cities = new HashSet<>();
        records.forEach(r -> CompletableFuture.supplyAsync(() -> parseFromCsvRecord(r), executorService)
                .thenAccept(c -> {if (c != null) cities.add(c);}));
        executorService.shutdown();
        // ok, this is lazy, but as the run takes a bit of time
        // I've not made it a priority to clean this up
        executorService.awaitTermination(5L, TimeUnit.MINUTES);
        repo.saveAll(cities);
        return repo.count();
    }

    private City parseFromCsvRecord(CSVRecord csvRecord) {
        // there's a footer line to ignore. Could configure the CSV parser, but this is easier
        if (csvRecord.size() < 3) {
            log.info("Invalid CSVRecord:" + csvRecord);
            return null;
        }
        log.info("Getting climate data for id {}, {}/{}", csvRecord.get(2), csvRecord.get(0), csvRecord.get(1));
        City city = getCity(Integer.valueOf(csvRecord.get(2)));
        if (city == null || city.hasMissingClimateData()) return null;
        city.setCountry(csvRecord.get(0));
        Integer population = populations.get(city.getCityId());
        if (population != null)
            city.setPopulation(population);
        return city;
    }


    private City getCity(Integer cityId) {
        try {
            return new RestTemplate().getForObject(String.format(citiesUrl, cityId), CityWrapper.class).getCity();
        } catch (Throwable t) {
            log.error("Couldn't create City object from input for city id {}", cityId, t);
            String goofy = new RestTemplate().getForObject(String.format(citiesUrl, cityId), String.class);
            log.error("goofy record: {}", goofy);
            return null;
        }
    }
}
