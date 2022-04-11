package org.pulcifer.climate.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.pulcifer.climate.model.serialization.CityJsonDeserializer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = CityJsonDeserializer::class)
@Document(collection = "city")
class City {
    @Id
    var id: String? = null
    var lang: String? = null
    var cityName: String? = null
    var country: String? = null
    var cityLatitude: BigDecimal? = null
    var cityLongitude: BigDecimal? = null
    var cityId: Int? = null
    var isCapital: Boolean? = null
    var stationName: String? = null
    var tourismURL: String? = null
    var tourismBoardName: String? = null
    var isDep: Boolean? = null
    var timeZone: String? = null
    var isDST: String? = null
    var climate: Climate? = null

    constructor() {}
    constructor(cityId: Int?) {
        this.cityId = cityId
    }

    override fun equals(b: Any?): Boolean {
        if (b !is City) return false
        val cityB = b
        return if (cityId == null || cityB.cityId == null) false else cityId == cityB.cityId
    }
}