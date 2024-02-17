package com.example.demo.data.demo.model

import com.example.demo.data.mlb.model.Game
import com.example.demo.data.mlb.model.GameResponse
import com.example.demo.data.weather.model.ForecastResponse

data class GameDateResponse(val date: String, val game: Game, val forecastResponse: ForecastResponse) {
    override fun toString(): String {
        return "Game Date Info: ${date} ${game.home} ${game.away} ${forecastResponse.period.temperature}${forecastResponse.period.temperatureUnit}"
    }
}