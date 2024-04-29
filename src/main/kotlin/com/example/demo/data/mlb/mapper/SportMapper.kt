package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object SportMapper {
    fun mapSport(json: String): Sport {
        try {
            val objectMapper = ObjectMapper()
            val teamNode = objectMapper.readTree(json)

            val id = teamNode["id"].asInt()
            val code = teamNode["code"].asText()
            val link = teamNode["link"].asText()
            val name = teamNode["name"].asText()
            val abbreviation = teamNode["abbreviation"].asText()
            val sortOrder = teamNode["sortOrder"].asInt()
            val activeStatus = teamNode["activeStatus"].asBoolean()


            return Sport(id,
                code,
                link,
                name,
                abbreviation,
                sortOrder,
                activeStatus)

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Sport Serialization Failure", e)
        }
        //TODO :
        // Double-Check this error propagation
        catch(e: Exception){
            throw SerializationFailedException("Sport Serialization Error", e)
        }
    }
}

