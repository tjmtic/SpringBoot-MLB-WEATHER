package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.TeamServiceResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object TeamResponseMapper {
    fun mapTeam(json: String): TeamServiceResponse {
        try {
            val objectMapper = ObjectMapper()
            val responseNode = objectMapper.readTree(json)

            val teamsNode = responseNode["teams"]
            val teams = teamsNode.map { TeamMapper.mapTeam(it.toString()) }

            //TODO ?
            return TeamServiceResponse(teams.first())

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("TeamResponse Serialization Error", e)
        }
    }
}