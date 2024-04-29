package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.StandardResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

class StandardResponseMapper {
    inline fun map(json: String): StandardResponse {
        try {
            val objectMapper = ObjectMapper()
            val responseNode = objectMapper.readTree(json)
            //Isolate Node for ItemList
            val gamesNode = responseNode["games"]
            val games = when(gamesNode){
                null -> null
                else -> { gamesNode.map { GameMapper.mapGame(it.toString()) } }
            }
            val venuesNode = responseNode["venues"]
            val venues = when(venuesNode){
                null -> null
                else -> { venuesNode.map { VenueMapper.mapVenue(it.toString()) } }
            }
            val teams = when(val teamsNode = responseNode["teams"]){
                null -> null
                else -> { teamsNode.map { TeamMapper.mapTeam(it.toString()) } }
            }

            val leaguesNode = responseNode["leagues"]
            val leagues = when(leaguesNode){
                null -> null
                else -> { leaguesNode.map { LeagueMapper.mapLeague(it.toString()) } }
            }

            val sportsNode = responseNode["sports"]
            val sports = when(sportsNode){
                null -> null
                else -> { sportsNode.map { SportMapper.mapSport(it.toString()) } }
            }

            val divisionsNode = responseNode["divisions"]
            val divisions = when(divisionsNode){
                null -> null
                else -> { divisionsNode.map { DivisionMapper.mapDivision(it.toString()) } }
            }



            return StandardResponse(games, venues, teams, leagues, sports, divisions)
        } catch (e: Exception){
            return StandardResponse(null, null, null, null, null, null)
        }
       // return res
    }

    fun <T> mapListForNode(node: String, json: String, mapIt: (String) -> T): List<T>? {
        try {
            val objectMapper = ObjectMapper()
            val responseNode = objectMapper.readTree(json)

            //Isolate Node for ItemList
            val listNode = responseNode[node]

            return listNode.map { mapIt(it.toString()) }

        } catch (e: JsonProcessingException) {
            throw SerializationFailedException("ListNode Serialization Error", e)
        }
    }
}