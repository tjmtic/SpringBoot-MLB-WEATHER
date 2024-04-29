package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.GameResponse
import com.example.demo.data.mlb.model.GamesResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

object GamesResponseMapper {
    fun mapGames(json: String): GamesResponse {
        try {
            val objectMapper = ObjectMapper()
            val gameNode = objectMapper.readTree(json)

            val gamesNode = gameNode["dates"][0]["games"]

            val games = gamesNode.map { GameMapper.mapGame(it.toString()) }

            return GamesResponse(games)
        } catch(e: JsonProcessingException){
        throw SerializationFailedException("GameResponse Serialization Error", e)
        }
    }
}