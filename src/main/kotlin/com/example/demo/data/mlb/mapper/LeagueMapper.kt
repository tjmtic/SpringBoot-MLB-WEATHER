package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object LeagueMapper {
    fun mapLeague(json: String): League? {
        try {
            val objectMapper = ObjectMapper()
            val leagueNode = objectMapper.readTree(json)
            val teamNode = leagueNode//["leagues"]

            println("MAPPING LEAGUE: $json")

            val id = teamNode["id"].asInt()
            val name = teamNode["name"].asText()
            val link = teamNode["link"].asText()
            val abbreviation = teamNode["abbreviation"].asText()
            val nameShort = teamNode["name"].asText()
            val seasonState = teamNode["seasonState"].asText()
            val hasWildCard = teamNode["hasWildCard"].asBoolean()
            val hasSplitSeason = teamNode["hasSplitSeason"].asBoolean()
            val numGames = teamNode["numGames"].asInt()
            val hasPlayoffPoints = teamNode["hasPlayoffPoints"].asBoolean()
            val numTeams = teamNode["numTeams"].asInt()
            val numWildcardTeams = teamNode["numWildcardTeams"].asInt()
            val season = teamNode["season"].asText()
            val orgCode = teamNode["orgCode"].asText()
            val conferencesInUse = teamNode["conferencesInUse"].asBoolean()
            val divisionsInUse = teamNode["divisionsInUse"].asBoolean()
            val sportId = teamNode["sport"]["id"].asInt()
            val sortOrder = teamNode["sortOrder"].asInt()
            val active = teamNode["active"].asBoolean()


            return League(id, name, link, abbreviation, nameShort, seasonState, hasWildCard, hasSplitSeason, numGames, hasPlayoffPoints, numTeams,
                numWildcardTeams, season, orgCode, conferencesInUse, divisionsInUse, sportId, sortOrder, active)

        } catch(e: JsonProcessingException){
            return null
            //throw SerializationFailedException("League Serialization Failure", e)
        }
        //TODO :
        // Double-Check this error propagation
        catch(e: Exception){
            return null
            //throw SerializationFailedException("League Serialization Error ${e.message}", e)
        }
    }
}

