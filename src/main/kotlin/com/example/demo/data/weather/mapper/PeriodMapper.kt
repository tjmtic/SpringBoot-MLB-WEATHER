package com.example.demo.data.weather.mapper

import com.example.demo.data.weather.model.Period
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object PeriodMapper {
    fun mapPeriod(json: String): Period {
        return try {
            val objectMapper = ObjectMapper()
            val periodNode = objectMapper.readTree(json)

            val number = periodNode["number"].asInt()
            val temperature = periodNode["temperature"].asText()
            val temperatureUnit = periodNode["temperatureUnit"].asText()
            val windSpeed = periodNode["windSpeed"].asText()
            val windDirection = periodNode["windDirection"].asText()
            val shortForecast = periodNode["shortForecast"].asText()


            Period(
                number,
                temperature,
                temperatureUnit,
                windSpeed,
                windDirection,
                shortForecast
            )
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Period Serialization Failure", e)
        } catch(e: Exception){
            throw SerializationFailedException("Period Serialization Error", e)
        }
    }
}
