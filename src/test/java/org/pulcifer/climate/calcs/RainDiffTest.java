package org.pulcifer.climate.calcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.repo.CityRepository;
import org.pulcifer.climate.svc.CityService;
import org.pulcifer.climate.svc.CityServiceTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class RainDiffTest {
    @Test
    public void testSortByRainfall() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File fileA = getFileFromClasspath("Rain_CityA.json");
        File fileB = getFileFromClasspath("Rain_CityB.json");
        File fileC = getFileFromClasspath("Rain_CityC.json");
        File fileD = getFileFromClasspath("Rain_CityD.json");

        SimilarCity root = new SimilarCity(mapper.readValue(fileA, City.class));
        SimilarCity b = new SimilarCity(mapper.readValue(fileB, City.class));
        SimilarCity c = new SimilarCity(mapper.readValue(fileC, City.class));
        SimilarCity d = new SimilarCity(mapper.readValue(fileD, City.class));
        List<SimilarCity> cities = new ArrayList<>();

        cities.add(b);
        cities.add(c);
        cities.add(d);

//        List<SimilarCity> sortedCities = new RainDiff().sortCitiesForRainFallAndRainDays(root, cities);
//
//        List<String> sortedIds = new ArrayList<>();
//
//        for (SimilarCity sc : sortedCities) {
//            sortedIds.add(sc.getCityName());
//        }
//
////        cityService = new CityService();
////        cityRepositoryMock = mock(CityRepository.class);
////        cityService.setRepo(cityRepositoryMock);
//
//        assertEquals(Arrays.asList("BBB","CCC","DDD"), sortedIds);



    }





    public static File getFileFromClasspath(String fileName) {
        // Get the file as a URL from the classpath
        URL resource = RainDiffTest.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found in classpath: " + fileName);
        }

        // Convert the URL to a File object
        return new File(resource.getFile());
    }

    public static void main(String[] args) {
        // Example usage
        String fileName = "example.txt"; // Make sure this file exists in the classpath
        try {
            File file = getFileFromClasspath(fileName);
            System.out.println("File Path: " + file.getAbsolutePath());
            System.out.println("File Exists: " + file.exists());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
