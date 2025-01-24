package org.pulcifer.climate.svc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.repo.CityRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CityServiceTest {

    private CityRepository cityRepositoryMock;
    private CityService cityService;

    private List<City> cities = new ArrayList<>();

    City city;

    @BeforeEach
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

//        ClassLoader classLoader = getClass().getClassLoader();
//        File fileA = new File(classLoader.getResource("/CityA.json").getFile());
//

        Object asdf = getClass().getClassLoader().getResource("CityA.json");
        String fileA = new String(getClass().getClassLoader().getResourceAsStream("CityA.json").readAllBytes());
        String fileB = new String(getClass().getClassLoader().getResourceAsStream("CityB.json").readAllBytes());
        String fileC = new String(getClass().getClassLoader().getResourceAsStream("CityC.json").readAllBytes());
        String fileD = new String(getClass().getClassLoader().getResourceAsStream("CityD.json").readAllBytes());

        city = mapper.readValue(fileA, City.class);
        City maybeSimilar = mapper.readValue(fileB, City.class);
        City outlier = mapper.readValue(fileC, City.class);
        City incomplete = mapper.readValue(fileD, City.class);

        cities.add(maybeSimilar);
        cities.add(outlier);
        cities.add(city);
        cities.add(incomplete);

        cityService = new CityService();
        cityRepositoryMock = mock(CityRepository.class);
        cityService.setRepo(cityRepositoryMock);
    }

    @Test
    public void getCityByIdTest() {
        when(cityRepositoryMock.findByCityId(111)).thenReturn(Optional.ofNullable(city));
        cityService.getCityById(111);
    }

    @Test
    public void citiesContainingTest() {
        List<City> cities = new ArrayList<>();
        cities.add(city);
        when(cityRepositoryMock.findByCityNameContaining("San Francisco")).thenReturn(Optional.ofNullable(cities));
        cityService.citiesContaining("San Francisco");
    }

    @Test
    public void retrieveSimilarCitiesTest() {
        when(cityRepositoryMock.findByCityId(111)).thenReturn(Optional.ofNullable(city));
        when(cityRepositoryMock.findAll()).thenReturn(cities);

        Similarity similarity = new Similarity();
        similarity.similarityMultiplier = BigDecimal.valueOf(1.2F);
        similarity.cityId = 111;
        similarity.minPop = 1;
        similarity.maxPop = 1000000;

        List<SimilarCity> similarCities = cityService.retrieveSimilarCities(similarity);

//        assertTrue(similarCities.get(0).getCityId().equals(111));
//        assertTrue(similarCities.get(1).getCityId().equals(222));
//        assertTrue(similarCities.size() == 2);
    }
}
