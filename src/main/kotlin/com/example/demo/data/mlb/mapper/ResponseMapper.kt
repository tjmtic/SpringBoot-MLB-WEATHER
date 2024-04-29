package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Division
import com.example.demo.data.mlb.model.Game
import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.StandardMapping
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.mlb.MlbServiceResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

class ResponseMapper<T> {
    inline fun <reified R> map(json: String): List<R?> {
        return when (R::class) {
            //R::class.isAssignableFrom(StandardMapping<T>::class) -> StandardMapping<T>::map(json) as R
            Team::class -> mapListForNode("teams", json, {nodeJson -> TeamMapper.mapTeam(nodeJson) }) as List<R>
            League::class -> mapListForNode("leagues", json, {nodeJson -> LeagueMapper.mapLeague(nodeJson) }) as List<R?>
            Sport::class -> mapListForNode("sports", json, {nodeJson -> SportMapper.mapSport(nodeJson) }) as List<R?>
            Division::class -> mapListForNode("divisions", json, {nodeJson -> DivisionMapper.mapDivision(nodeJson) }) as List<R?>

            Game::class -> mapListForNode("games", json, { nodeJson -> GameMapper.mapGame(nodeJson) }) as List<R>
            Venue::class -> mapListForNode("venues", json, { nodeJson -> VenueMapper.mapVenue(nodeJson) }) as List<R>

            else -> throw IllegalArgumentException("Unsupported type: ${R::class.simpleName}")
        }
       // return res
    }

    fun <T> mapListForNode(node: String, json: String, mapIt: (String) -> T): List<T> {
        try {
            val objectMapper = ObjectMapper()
            val responseNode = objectMapper.readTree(json)

            val listNode = responseNode[node]
            val list = listNode.map { mapIt(it.toString()) }

            return list

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("ListNode Serialization Error", e)
        }
    }
}