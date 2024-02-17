package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.VenueResponse
import com.example.demo.data.mlb.model.VenuesResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object VenuesResponseMapper {
    fun mapVenues(json: String): VenuesResponse {
        try {
            val objectMapper = ObjectMapper()
            val venueNode = objectMapper.readTree(json)

            val venuesNode = venueNode["venues"]
            val venues = venuesNode.map { VenueMapper.mapVenue(it.toString()) }

            //TODO : Remove this, and response values
            val lat = venues.first().venueLocation.defaultCoordinates.latitude
            val lon = venues.first().venueLocation.defaultCoordinates.longitude
            val name = venues.first().name
            val city = venues.first().venueLocation.city

            return VenuesResponse(venues, name, city, lat, lon)

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("VenueResponse Serialization Error", e)
        }
    }
}