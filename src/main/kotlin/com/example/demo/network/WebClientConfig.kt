package com.example.demo.network

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/**
 * Network WebClient Configuration
 */
@Configuration
class WebClientConfig {

    @Autowired
    private lateinit var mlbUrl: String
    @Autowired
    private lateinit var weatherUrl: String

    @Bean
    fun MlbWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(mlbUrl)
            .build()
    }

    @Bean
    fun WeatherWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(weatherUrl)
            .build()
    }

}