package com.example.demo.data.weather.model

data class Period(val number: Int,
                  val temperature: String,
                  val temperatureUnit: String,
                  val windSpeed: String,
                  val windDirection: String,
                  val shortForecast: String
)