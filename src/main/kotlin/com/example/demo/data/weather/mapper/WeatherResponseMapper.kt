package com.example.demo.data.weather.mapper

import com.example.demo.data.weather.model.WeatherResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object WeatherResponseMapper {
    fun mapWeather(json: String): WeatherResponse {
        try {
            val objectMapper = ObjectMapper()
            val weatherNode = objectMapper.readTree(json)
            val propertiesNode = weatherNode["properties"]

            val cwaNode = propertiesNode["cwa"].asText()
            val gridXNode = propertiesNode["gridX"].asText()
            val gridYNode = propertiesNode["gridY"].asText()
            val forecastNode = propertiesNode["forecast"].asText()

            return WeatherResponse(cwaNode, gridXNode, gridYNode, forecastNode)
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("WeatherResponse Serialization Failure", e)
        } catch(e: Exception){
            throw SerializationFailedException("WeatherResponse Serialization Error", e)
        }
    }
}