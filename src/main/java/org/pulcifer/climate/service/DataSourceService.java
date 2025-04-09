package org.pulcifer.climate.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.pulcifer.climate.model.CityMeta;
import org.pulcifer.climate.model.CityWeather;
import org.pulcifer.climate.repo.CityMetaRepository;
import org.pulcifer.climate.repo.CityWeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class DataSourceService {

    Logger logger = LoggerFactory.getLogger(getClass());

//    @Autowired
//    DataLoaderService loaderService;
    @Autowired
    CityMetaRepository metaRepo;
    @Autowired
    CityWeatherRepository weatherRepo;
    List<CityMeta> metasToRetrieve = new ArrayList<>();
    RestTemplate httpClient = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

    public List<CityWeather> loadWeatherData() {
        long start = new Date().getTime();
        loadSeedV2();
        metasToRetrieve = metaRepo.findAll();
        long elapsed = new Date().getTime() - start;
        executeTheRetrievals();
        logger.info("Elapsed: {}", elapsed);
        return List.of();
    }

    private CityWeather downloadCityWeather(CityMeta meta) {
        ResponseEntity<JsonNode> weatherResponse = httpClient.getForEntity(buildQueryString(meta), JsonNode.class);
        logger.info("Status Code: {}", weatherResponse.getStatusCode());
        JsonNode jsonNode = weatherResponse.getBody();
        return mapper.convertValue(jsonNode, CityWeather.class);
    }

    protected void loadSeedV2() {

        metaRepo.deleteAll();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("SeedCitiesV2.csv");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(streamReader)) {
            String line = reader.readLine(); // skip header line
            while ((line = reader.readLine()) != null) {
                CityMeta cityMeta = new CityMeta(line);
//                cityLocations.put(cityMeta.getCityId(), cityMeta);
                metaRepo.save(cityMeta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dumpData(CityWeather weather, CityMeta meta) throws JsonProcessingException {
        String path = "/Users/jdp/climate-data/%s.json";
        String weatherJson = writer.writeValueAsString(weather);
        try (FileWriter fw = new FileWriter(String.format(path, meta.getCityId()))) {
            fw.write(weatherJson);
        } catch (IOException e) {
            logger.error("IO error: {}", e.getMessage());
        }
    }


    private void uploadData(CityWeather weather, CityMeta meta) throws JsonProcessingException {


        weather.setSeedId(meta.getCityId());
        weather.setCity(meta.getCity());
        weather.setCountry(meta.getCountry());


        weatherRepo.save(weather);
        logger.info("Stored weather for city {}", weather.getSeedId());
    }

    private String buildQueryString(CityMeta meta) {
        String host = "https://weather.visualcrossing.com";
        String endpoint = "VisualCrossingWebServices/rest/services/timeline";
        String timeBound = "2020-01-01/2020-12-31";
        String apiKey = "77PV8JW7X7AHFJLCPD6SPV5ZK";
        String templateUrl = "%s/%s/%s,%s/%s?key=%s&unitGroup=us&include=hours,obs,remote&locationNames=%s";
        return String.format(templateUrl, host, endpoint, meta.getLatitude(), meta.getLongitude(), timeBound, apiKey, meta.getCityId());
    }

    private void singleExecutor(CityMeta meta) {
        CityWeather weather = downloadCityWeather(meta);
        try {
            if (weather != null) {
                dumpData(weather, meta);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info("Loaded city {}, {}", meta.getCity(), meta.getCountry());
    }


    private void executeTheRetrievals() {
weatherRepo.deleteAll();
        int i = 0;
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            for (CityMeta city : metasToRetrieve) {
//                Thread t = new Thread(() -> singleExecutor(city));
//                executor.execute(t);
//                break;
                singleExecutor(city);

            }
            awaitTerminationAfterShutdown(executor);
        }
        logger.info("queued {} city weather's to retrieve", i);
    }

    void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(1, TimeUnit.HOURS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
