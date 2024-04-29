package com.example.demo.data.mlb.mapper

import com.example.demo.service.mlb.request.league.LeaguesResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object LeaguesResponseMapper {
    fun mapLeagues(json: String): LeaguesResponse {
        try {
            println("MAPPING TEAMS!!!")
            val objectMapper = ObjectMapper()
            val responseNode = objectMapper.readTree(json)

            val teamsNode = responseNode["teams"]
            val teams = teamsNode.map { TeamMapper.mapTeam(it.toString()) }

            return LeaguesResponse(teams)

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("TeamsResponse Serialization Error", e)
        }
    }
}