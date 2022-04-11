package org.pulcifer.climate.model.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.pulcifer.climate.model.City
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import java.math.BigDecimal
import com.fasterxml.jackson.databind.DeserializationFeature
import kotlin.Throws
import java.io.IOException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * All this to handle goofy source data. I hope python devs
 * never see this or they won't stop making fun of how
 * much typing we have to do as Java professionals
 */
class CityJsonDeserializer : JsonDeserializer<City>() {
    companion object {
        private val objectMapper = ObjectMapper()

        init {
            val module = SimpleModule()
            module.addDeserializer<BigDecimal>(
                BigDecimal::class.java,
                CustomBigDecimalDeserializer()
            )
            objectMapper.registerModule(module)
            objectMapper.addMixIn(City::class.java, DefaultJsonDeserializer::class.java)
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): City {
        return objectMapper.readValue(jsonParser, City::class.java)
    }

    @JsonDeserialize
    private interface DefaultJsonDeserializer { // Reset default json deserializer
    }
}