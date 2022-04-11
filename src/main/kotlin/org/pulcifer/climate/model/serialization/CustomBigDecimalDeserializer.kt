package org.pulcifer.climate.model.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.slf4j.Logger
import java.math.BigDecimal
import kotlin.Throws
import java.io.IOException
import org.slf4j.LoggerFactory
import java.lang.NumberFormatException

/**
 * For this project, we need an explicit Deserializer for BigDecimal
 * values, as the source data isn't 100% consistent. Specifically, nulls
 * are not handled well (blanks, nulls, 'NULL''s are all used), and
 * 0..1 values are represented as "TR" (trace).
 */
class CustomBigDecimalDeserializer : JsonDeserializer<BigDecimal?>() {
    var log: Logger = LoggerFactory.getLogger(javaClass)
    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, ctx: DeserializationContext): BigDecimal? {
        val value = jsonParser.readValueAs(String::class.java)
        if (value == null || value.isBlank() || "NULL" == value) return null
        // "TR" indicates 'trace' amount. Effectively zero for our purposes
        return if ("TR" == value) BigDecimal.ZERO else try {
            BigDecimal(value.trim { it <= ' ' })
        } catch (ne: NumberFormatException) {
            // If a value cannot be formatted, we want to log it, but not
            // stop processing, so we default to ZERO.
            log.error(
                String.format(
                    "Value %s for field %s is not compatible with BigDecimal, therefore setting to default value (0)",
                    value, jsonParser.currentName
                )
            )
            BigDecimal.ZERO
        }
    }
}