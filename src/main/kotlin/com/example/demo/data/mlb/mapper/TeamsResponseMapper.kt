package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.TeamsResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object TeamsResponseMapper {
    fun mapTeams(json: String): TeamsResponse {
        try {
            println("MAPPING TEAMS!!!")
            val objectMapper = ObjectMapper()
            val responseNode = objectMapper.readTree(json)

            val teamsNode = responseNode["teams"]
            val teams = teamsNode.map { TeamMapper.mapTeam(it.toString()) }

            return TeamsResponse(teams)

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("TeamsResponse Serialization Error", e)
        }
    }
}