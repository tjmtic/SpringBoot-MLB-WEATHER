package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Venue
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object VenueMapper {
    fun mapVenue(json: String): Venue {
        try {
            val objectMapper = ObjectMapper()
            val venueNode = objectMapper.readTree(json)

            val id = venueNode["id"].asInt()
            val name = venueNode["name"].asText()
            val link = venueNode["link"].asText()
            val locationNode = venueNode["location"]
            val location = when(locationNode) {
                null -> null
                else -> LocationMapper.mapLocation(locationNode.toString())
            }

            val active = venueNode["active"]?.asBoolean()
            val season = venueNode["season"]?.asText()

            return Venue(id, name, link, location, active, season)
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Venue Serialization Error", e)
        }
    }
}
