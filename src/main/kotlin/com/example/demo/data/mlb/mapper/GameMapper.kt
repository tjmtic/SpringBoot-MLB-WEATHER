package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Game
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object GameMapper {
    fun mapGame(json: String): Game {
        try {
            val objectMapper = ObjectMapper()
            val gameNode = objectMapper.readTree(json)

            val teamsNode = gameNode["teams"]

            val homeNode = teamsNode["home"]
            val home = homeNode["team"]["name"].asText()
            val away = teamsNode["away"]["team"]["name"].asText()

            val venueNode = gameNode["venue"]
            val venueId = venueNode["id"].asText()

            return Game(venueId, home, away)
        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Game Serialization Error", e)
        }
    }
}
