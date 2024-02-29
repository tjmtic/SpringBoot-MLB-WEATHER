package com.example.demo.data.weather.mapper

import com.example.demo.data.weather.model.ForecastResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object ForecastResponseMapper {
    fun mapForecast(json: String): ForecastResponse {
        try {
            val objectMapper = ObjectMapper()
            val forecastNode = objectMapper.readTree(json)

            val propertiesNode = forecastNode["properties"]
            val periodNode = propertiesNode["periods"]

            val periods = periodNode.map { PeriodMapper.mapPeriod(it.toString()) }

            return ForecastResponse(periods)
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("ForecastResponse Serialization Failure", e)
        } catch(e: Exception){
            throw SerializationFailedException("ForecastResponse Serialization Error", e)
        }
    }
}