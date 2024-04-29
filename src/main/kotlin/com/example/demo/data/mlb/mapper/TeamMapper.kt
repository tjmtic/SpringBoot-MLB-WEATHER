package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object TeamMapper {
    fun mapTeam(json: String): Team? {
        try {
            val objectMapper = ObjectMapper()
            val teamNode = objectMapper.readTree(json)

            val id = teamNode["id"]?.asInt()
            val name = teamNode["name"]?.asText()
            val link = teamNode["link"]?.asText()

            val venueNode = teamNode["venue"]
            val venueId = venueNode["id"]?.asInt()
            val allStarStatus = teamNode["allStarStatus"]?.asText()
            val season = teamNode["season"]?.asInt()
            val teamCode = teamNode["teamCode"]?.asText()
            val fileCode = teamNode["fileCode"]?.asText()
            val abbreviation = teamNode["abbreviation"]?.asText()
            val teamName = teamNode["teamName"]?.asText()
            val locationName = teamNode["locationName"]?.asText()
            val firstYearOfPlay = teamNode["firstYearOfPlay"]?.asText()

            val leagueNode = teamNode["league"]
            val leagueId = leagueNode["id"]?.asInt()

            val divisionNode = teamNode["division"]
            val divisionId = when(divisionNode) {
                null -> null
                else -> divisionNode["id"]?.asInt()
            }

            val sportNode = teamNode["sport"]
            val sportId = sportNode["id"]?.asInt()
            val shortName = teamNode["shortName"]?.asText()
            val parentOrgName = teamNode["parentOrgName"]?.asText()
            val parentOrgId = teamNode["parentOrgId"]?.asInt()
            val franchiseName = teamNode["franchiseName"]?.asText()
            val clubName = teamNode["clubName"]?.asText()
            val active = teamNode["active"]?.asBoolean()


            return Team(id,
                name,
                link,
                venueId,
                allStarStatus,
                season,
                teamCode,
                fileCode,
                abbreviation,
                teamName,
                locationName,
                firstYearOfPlay,
                leagueId,
                divisionId,
                sportId,
                shortName,
                parentOrgName,
                parentOrgId,
                franchiseName,
                clubName,
                active)

        } catch(e: JsonProcessingException){
            println("${e.message}")
            return null
            //throw SerializationFailedException("Team Serialization Failure", e)
        }
        //TODO :
        // Double-Check this error propagation
        catch(e: Exception){
            println("${e.message}")
            return null
            //throw SerializationFailedException("Team Serialization Error", e)
        }
    }
}

