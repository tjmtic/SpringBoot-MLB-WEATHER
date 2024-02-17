package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.DefaultCoordinates
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object DefaultCoordinatesMapper {
    fun mapDefaultCoordinates(json: String): DefaultCoordinates {
        try {
            val objectMapper = ObjectMapper()
            val locationNode = objectMapper.readTree(json)

            //Get Node Values
            val lat = locationNode["latitude"].asDouble()
            val lon = locationNode["longitude"].asDouble()

            //Rounding necessary for Weather API
            //val latitude = BigDecimal(lat).setScale(4, RoundingMode.DOWN).toDouble()
            //val longitude = BigDecimal(lon).setScale(4, RoundingMode.DOWN).toDouble()

            return DefaultCoordinates(lat, lon)
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("DefaultCoordinates Serialization Error", e)
        }
    }
}