package com.example.demo.service.mlb

import com.example.demo.data.mlb.model.GameVenueResponse
import com.example.demo.data.mlb.model.GamesResponse
import com.example.demo.data.mlb.model.TeamServiceResponse
import com.example.demo.data.mlb.model.TeamsResponse
import com.example.demo.data.mlb.model.VenueResponse
import com.example.demo.data.mlb.model.VenuesResponse

interface MlbService {

    /**
     * Action: Retrieve team associated with a specific MLB identifier.
     * Parameters:
     * - id: The identifier representing the MLB entity for which team is to be retrieved.
     * Returns:
     * - TeamResponse: An object containing information about the team associated with the specified MLB entity.
     */
    fun getTeam(id: String): TeamServiceResponse

    /**
     * Action: Retrieve teams associated with MLB.
     *
     * Returns:
     * - TeamResponse: An object containing information about the teams associated with the MLB.
     */
    fun getTeams(): TeamsResponse
    /**
     * Action: Retrieve venue associated with a specific MLB identifier.
     * Parameters:
     * - id: The identifier representing the MLB entity for which venues are to be retrieved.
     * Returns:
     * - VenueResponse: An object containing information about the venues associated with the specified MLB entity.
     */
    fun getVenue(id: String): VenueResponse

    /**
     * Action: Retrieve venues associated with MLB.
     *
     * Returns:
     * - VenueResponse: An object containing information about the venues associated with the MLB.
     */
    fun getVenues(): VenuesResponse

    /**
     * Action: Retrieve MLB games within a specified date range.
     * Parameters:
     * - id: The identifier representing the MLB VENUE entity for which games are to be retrieved.
     * - startDate: The start date of the date range for which games are to be retrieved.
     * - endDate: The end date of the date range for which games are to be retrieved.
     * Returns:
     * - GamesResponse: An object containing information about the MLB games within the specified date range.
     */
    fun getGames(id: String, startDate: String, endDate: String) : GamesResponse

    /**
     * Action: Retrieve venue information associated with a specific MLB game.
     * Parameters:
     * - id: The identifier representing the MLB venue for which venue information is to be retrieved.
     * - date: The date of the MLB game for which venue information is to be retrieved.
     * Returns:
     * - GameVenueResponse: An object containing venue information associated with the specified MLB game.
     */
    fun getVenueForGame(id:String, date:String): GameVenueResponse
}