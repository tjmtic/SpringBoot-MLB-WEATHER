package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.GameResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object GameResponseMapper {
    fun mapGame(json: String): GameResponse {
        try {
            val objectMapper = ObjectMapper()
            val gameNode = objectMapper.readTree(json)

            val gamesNode = gameNode["dates"][0]["games"]

            val games = gamesNode.map { GameMapper.mapGame(it.toString()) }

            return GameResponse(games.first().venueId, games.first().home, games.first().away)
        } catch(e: JsonProcessingException){
        throw SerializationFailedException("GameResponse Serialization Error", e)
        }
    }
}