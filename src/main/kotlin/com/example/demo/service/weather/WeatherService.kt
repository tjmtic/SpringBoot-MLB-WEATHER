package com.example.demo.service.weather

import com.example.demo.data.weather.model.ForecastResponse


interface WeatherService {
    /**
     * Action: Retrieve the forecast for a specific location identified by latitude and longitude.
     * Parameters:
     * - latitude: The latitude coordinate of the location.
     * - longitude: The longitude coordinate of the location.
     * Returns:
     * - ForecastResponse: An object containing the forecast information for the specified location.
     */
    fun getForecastForLocation(latitude: Double, longitude: Double): ForecastResponse
}