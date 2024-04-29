package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object TeamMapper {
    fun mapTeam(json: String): Team {
        try {
            val objectMapper = ObjectMapper()
            val teamNode = objectMapper.readTree(json)

            val id = teamNode["id"].asInt()
            val name = teamNode["name"].asText()
            val link = teamNode["link"].asText()
            val venueId = teamNode["venue"]["id"].asInt()
            val allStarStatus = teamNode["allStarStatus"].asText()
            val season = teamNode["season"].asInt()
            val teamCode = teamNode["teamCode"].asText()
            val fileCode = teamNode["fileCode"].asText()
            val abbreviation = teamNode["abbreviation"].asText()
            val teamName = teamNode["teamName"].asText()
            val locationName = teamNode["locationName"].asText()
            val firstYearOfPlay = teamNode["firstYearOfPlay"].asText()
            val leagueId = teamNode["league"]["id"].asInt()
            val divisionId = teamNode["division"]["id"].asInt()
            val sportId = teamNode["sport"]["id"].asInt()
            val shortName = teamNode["shortName"].asText()
            val parentOrgName = teamNode["parentOrgName"].asText()
            val parentOrgId = teamNode["parentOrgId"].asInt()
            val franchiseName = teamNode["franchiseName"].asText()
            val clubName = teamNode["clubName"].asText()
            val active = teamNode["active"].asBoolean()


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
            throw SerializationFailedException("Team Serialization Failure", e)
        }
        //TODO :
        // Double-Check this error propagation
        catch(e: Exception){
            throw SerializationFailedException("Team Serialization Error", e)
        }
    }
}

