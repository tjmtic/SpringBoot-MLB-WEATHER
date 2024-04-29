package com.example.demo.service.weather

import com.example.demo.data.weather.mapper.ForecastResponseMapper
import com.example.demo.data.weather.mapper.WeatherResponseMapper
import com.example.demo.data.weather.model.ForecastResponse
import com.example.demo.data.weather.model.WeatherResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.serializer.support.SerializationFailedException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class WeatherServiceImpl(@Qualifier("WeatherWebClient") val webClient: WebClient): WeatherService {

    companion object {
        const val NOT_FOUND_LOCATION = "Location: Not Found"
        const val NOT_FOUND_FORECAST = "Forecast: Not Found"
    }

    internal fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return try {
            getWeatherRequest(latitude, longitude).block() ?: throw WeatherServiceException(NOT_FOUND_LOCATION)
        } catch (e: WeatherServiceException){
            throw WeatherServiceException(NOT_FOUND_LOCATION, e)
        } catch (e: Exception){
            throw WeatherServiceException("${e.message}", e)
        }
    }

    private fun getWeatherRequest(latitude: Double, longitude: Double): Mono<WeatherResponse> {
        //Transform Input Data for Request
        val lat = BigDecimal(latitude).setScale(4, RoundingMode.DOWN).toDouble()
        val lon = BigDecimal(longitude).setScale(4, RoundingMode.DOWN).toDouble()

        return webClient.get()
            .uri("points/$lat,$lon")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ WeatherResponseMapper.mapWeather(it) }
            .onErrorMap {
                WeatherServiceException("${it.message}", it)
            }
    }

    internal fun getForecast(caw: String, gridX: String, gridY: String, forecast: String?): ForecastResponse {
        return try {
            getForecastRequest(caw, gridX, gridY, forecast).block() ?: throw WeatherServiceException(NOT_FOUND_FORECAST)
        } catch (e: WeatherServiceException){
            throw WeatherServiceException(NOT_FOUND_FORECAST, e)
        } catch (e: Exception){
            throw WeatherServiceException("${e.message}", e)
        }
    }

    private fun getForecastRequest(caw: String, gridX: String, gridY: String, forecast: String?): Mono<ForecastResponse> {
        return webClient.get()
            .uri("gridpoints/$caw/$gridX,$gridY/forecast")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ForecastResponseMapper.mapForecast(it) }
            .onErrorMap {
                WeatherServiceException("${it.message}", it)
            }
    }

    override fun getForecastForLocation(latitude: Double, longitude: Double): ForecastResponse {
        return getWeather(latitude, longitude).let {
            getForecast(it.cwa, it.gridX, it.gridY, it.forecast)
        }
    }
}

class WeatherServiceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)