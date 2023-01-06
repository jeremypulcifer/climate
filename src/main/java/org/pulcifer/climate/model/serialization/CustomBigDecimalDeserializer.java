package org.pulcifer.climate.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * For this project, we need an explicit Deserializer for BigDecimal
 * values, as the source data isn't 100% consistent. Specifically, nulls
 * are not handled well (blanks, nulls, 'NULL''s are all used), and
 * 0..1 values are represented as "TR" (trace).
 */
public class CustomBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        String value = jsonParser.readValueAs(String.class);
        if (value == null || value.isBlank() || "NULL".equals(value))
            return null;
        // "TR" indicates 'trace' amount. Effectively zero for our purposes
        if ("TR".equals(value)) return BigDecimal.ZERO;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException ne) {
            // If a value cannot be formatted, we want to log it, but not
            // stop processing, so we default to ZERO.
            log.error(String.format(
                    "Value %s for field %s is not compatible with BigDecimal, therefore setting to default value (0)",
                    value, jsonParser.getCurrentName()));
            return BigDecimal.ZERO;
        }
    }
}
