package com.example.demo.data.demo.model

import com.example.demo.data.mlb.model.Venue
import com.example.demo.data.weather.model.ForecastResponse

data class VenueForecastResponse(val venue: Venue, val forecastResponse: ForecastResponse) {
    override fun toString() : String {
        return "Venue Forecast Info: ${venue.venueLocation.city} ${venue.name} ${forecastResponse.period.windSpeed}"
    }
}