package org.pulcifer.climate.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.pulcifer.climate.model.City;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * All this to
 *
 *
 */
public class CityJsonDeserializer extends JsonDeserializer<City> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BigDecimal.class, new CustomBigDecimalDeserializer());
        objectMapper.registerModule(module);
        objectMapper.addMixIn(City.class, DefaultJsonDeserializer.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public City deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return objectMapper.readValue(jsonParser, City.class);
    }

    @JsonDeserialize
    private interface DefaultJsonDeserializer {
        // Reset default json deserializer
    }

}