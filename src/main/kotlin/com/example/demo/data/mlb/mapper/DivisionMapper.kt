package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Division
import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object DivisionMapper {
    fun mapDivision(json: String): Division {
        try {
            val objectMapper = ObjectMapper()
            val teamNode = objectMapper.readTree(json)

            val id = teamNode["id"].asInt()
            val name = teamNode["name"].asText()
            val season = teamNode["season"].asText()
            val nameShort = teamNode["name"].asText()
            val link = teamNode["link"].asText()
            val abbreviation = teamNode["abbreviation"].asText()
            val leagueId = teamNode["league"]["id"].asInt()
            val sportId = teamNode["sport"]["id"].asInt()
            val hasWildcard = teamNode["hasWildcard"].asBoolean()
            val sortOrder = teamNode["sortOrder"].asInt()
            val active = teamNode["active"].asBoolean()


            return Division(id,name, season, nameShort, link, abbreviation, leagueId, sportId, hasWildcard, sortOrder, active)

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Division Serialization Failure", e)
        }
        //TODO :
        // Double-Check this error propagation
        catch(e: Exception){
            throw SerializationFailedException("Division Serialization Error", e)
        }
    }
}

