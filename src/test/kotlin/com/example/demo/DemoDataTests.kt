package com.example.demo

import com.example.demo.data.weather.mapper.PeriodMapper
import com.example.demo.data.weather.model.Period
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.serializer.support.SerializationFailedException


@SpringBootTest
class DemoDataTests {

    companion object {
        const val PERIOD_STRING_VALID = "{\"number\":1,\"name\":\"Monday Night\",\"startTime\":\"2022-02-19T18:00:00-05:00\",\"endTime\":\"2022-02-20T06:00:00-05:00\",\"isDaytime\":false,\"temperature\":32,\"temperatureUnit\":\"F\",\"temperatureTrend\":null,\"probabilityOfPrecipitation\":{\"unitCode\":\"wmoUnit:percent\",\"value\":null},\"dewpoint\":{\"unitCode\":\"wmoUnit:degC\",\"value\":-1.6666666666666667},\"relativeHumidity\":{\"unitCode\":\"wmoUnit:percent\",\"value\":72},\"windSpeed\":\"12 mph\",\"windDirection\":\"SW\",\"icon\":\"https://api.weather.gov/icons/land/night/sct?size=medium\",\"shortForecast\":\"Partly Cloudy\",\"detailedForecast\":\"Partly cloudy, with a low around 32.\"}"
        const val PERIOD_STRING_INVALID = "{\"endTime\":\"2024-02-20T06:00:00-05:00\",\"isDaytime\":false,\"temperatureUnit\":\"F\",\"temperatureTrend\":null,\"probabilityOfPrecipitation\":{\"unitCode\":\"wmoUnit:percent\",\"value\":null},\"dewpoint\":{\"unitCode\":\"wmoUnit:degC\",\"value\":-1.6666666666666667},\"relativeHumidity\":{\"unitCode\":\"wmoUnit:percent\",\"value\":72}}"
        const val PERIOD_STRING_NULL = ""
        val defaultPeriod = Period(1, "32", "F", "12 mph", "SW", "Partly Cloudy")
    }

    @Test
    fun `test mapper - period - SUCCESS`(){
        val testPeriod = PeriodMapper.mapPeriod(PERIOD_STRING_VALID)
        assertEquals(testPeriod, defaultPeriod)
    }

    @Test
    fun `test mapper - period - ERROR - INVALID`(){
        try {
            val testPeriod = PeriodMapper.mapPeriod(PERIOD_STRING_INVALID)
            fail("Expected Exception was not thrown")
        } catch (e: SerializationFailedException) {
            assertEquals("Period Serialization Error", e.message)
        }

    }

    @Test
    fun `test mapper - period - ERROR - NULL`(){
        try{
            val testPeriod = PeriodMapper.mapPeriod(PERIOD_STRING_NULL)
            fail("Expected Exception was not thrown")
        } catch (ex: SerializationFailedException) {
            assertEquals("Period Serialization Error", ex.message)
        }
    }
}