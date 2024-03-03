package com.example.demo.service.fb

import com.example.demo.data.weather.model.ForecastResponse


interface FbService {
    /**
     * Action: .
     * Parameters:
     * - :
     * Returns:
     * - :
     */
    fun postIgItem(imageUrl: String, caption: String): String
}