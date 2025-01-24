package org.pulcifer.climate.svc.diffCalc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiffCalulatorTest {

    @Test
    public void calcDiffTest() {
        DiffCalculator diffCalc = new DiffCalculator();

        BigDecimal value1 = new BigDecimal("100");
        BigDecimal value2 = new BigDecimal("101");
        BigDecimal value3 = new BigDecimal("200");
        assertEquals(new BigDecimal("1.01"), diffCalc.calcDiff(value1, value2));
        assertEquals(new BigDecimal("2.00"), diffCalc.calcDiff(value1, value3));
        assertEquals(new BigDecimal("1.98"), diffCalc.calcDiff(value2, value3));
    }
}
