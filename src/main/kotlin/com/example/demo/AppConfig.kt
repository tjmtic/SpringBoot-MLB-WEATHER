package com.example.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Application level Configuration
 */
@Configuration
class AppConfig {

    @Value("\${WEATHER_URL}")
    private lateinit var defaultWeatherUrl: String
    @Bean
    fun weatherUrl(): String {
        return defaultWeatherUrl
    }


    @Value("\${MLB_URL}")
    private lateinit var mlbUrl: String
    @Bean
    fun mlbUrl(): String {
        return mlbUrl
    }

    @Value("\${FB_URL}")
    private lateinit var fbUrl: String
    @Bean
    fun fbUrl(): String {
        return fbUrl
    }

    @Value("\${FB_TOKEN}")
    private lateinit var fbToken: String
    @Bean
    fun fbToken(): String {
        return fbToken
    }

    @Value("\${FB_ID}")
    private lateinit var fbId: String
    @Bean
    fun fbId(): String {
        return fbId
    }

}