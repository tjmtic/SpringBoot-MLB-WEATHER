package com.example.demo.service.demo

import com.example.demo.data.demo.model.GameDateResponse
import com.example.demo.data.demo.model.VenueForecastResponse

interface DemoService {
    /**
     * Action: Retrieve forecast information for a specific venue.
     * Parameters:
     * - id: The identifier representing the MLB venue for which forecast information is to be retrieved.
     * Returns:
     * - VenueForecastResponse: An object containing forecast information for the specified venue.
     */
    fun getForecastForVenue(id: String): VenueForecastResponse

    /**
     * Action: Retrieve games associated with a specific date.
     * Parameters:
     * - id: The identifier representing the VENUE entity for which games are to be retrieved.
     * - date: The date for which games are to be retrieved.
     * Returns:
     * - GameDateResponse: An object containing information about games associated with the specified date.
     */
    fun getGamesByDate(id:String, date:String): GameDateResponse
}