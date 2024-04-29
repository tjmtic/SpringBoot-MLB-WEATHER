package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.VenueLocation
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object LocationMapper {
    fun mapLocation(json: String): VenueLocation {
        try {
            val objectMapper = ObjectMapper()
            val locationNode = objectMapper.readTree(json)

            val address = locationNode["address1"].asText()
            val city = locationNode["city"].asText()
            val state = locationNode["state"].asText()
            val dc =
                DefaultCoordinatesMapper.mapDefaultCoordinates(locationNode["defaultCoordinates"].toString())

            return VenueLocation(address, city, state, dc)
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Location Serialization Error", e)
        }
    }
}