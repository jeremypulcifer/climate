package org.pulcifer.climate.svc.diffCalc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.repo.CityRepository;
import org.pulcifer.climate.svc.CityService;
import org.pulcifer.climate.svc.CityServiceTest;
import org.pulcifer.climate.svc.Similarity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinAndMaxTempDiffTest {


    private List<City> cities = new ArrayList<>();

    City city;
    City maybeSimilar;

    @BeforeEach
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File fileA = new File(MinAndMaxTempDiffTest.class.getClassLoader().getResource("CityA.json").getFile());
        File fileB = new File(MinAndMaxTempDiffTest.class.getClassLoader().getResource("CityB.json").getFile());
//        File fileC = new File(MinAndMaxTempDiffTest.class.getClassLoader().getResource("CityC.json").getFile());
//        File fileD = new File(MinAndMaxTempDiffTest.class.getClassLoader().getResource("CityD.json").getFile());

        city = mapper.readValue(fileA, City.class);
        maybeSimilar = mapper.readValue(fileB, City.class);
//        City outlier = mapper.readValue(fileC, City.class);
//        City incomplete = mapper.readValue(fileD, City.class);

        cities.add(maybeSimilar);
//        cities.add(outlier);
        cities.add(city);
//        cities.add(incomplete);
    }

    @Test
    public void getCityByIdTest() {
        MinAndMaxTempDiff diff = new MinAndMaxTempDiff();

        SimilarCity city1 = new SimilarCity(city);
        SimilarCity city2 = new SimilarCity(maybeSimilar);

        BigDecimal diffValue = diff.calcDiff(city1, city2);
        System.out.println(diffValue);
//        when(cityRepositoryMock.findByCityId(111)).thenReturn(Optional.ofNullable(city));
//        cityService.getCityById(111);
    }

}
