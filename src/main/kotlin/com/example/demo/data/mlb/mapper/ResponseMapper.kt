package com.example.demo.data.mlb.mapper

import com.example.demo.data.mlb.model.Division
import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.StandardMapping
import com.example.demo.data.mlb.model.Team
import com.example.demo.service.mlb.MlbServiceResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException

class ResponseMapper<T> {
    inline fun <reified R> map(json: String): MlbServiceResponse<R> {
        val res: R = when (R::class) {
            //R::class.isAssignableFrom(StandardMapping<T>::class) -> StandardMapping<T>::map(json) as R
            Team::class -> TeamMapper.mapTeam(json) as R
            League::class -> LeagueMapper.mapLeague(json) as R
            Sport::class -> SportMapper.mapSport(json) as R
            Division::class -> LeagueMapper.mapLeague(json) as R
            // Add more mappings for other types as needed
            else -> throw IllegalArgumentException("Unsupported type: ${R::class.simpleName}")
        }
        return MlbServiceResponse(res, null)
    }
}
/*
class ResponseMapper<T> {
    fun map(json: String): StandardResponse<T> {

        val res = when (T) {
            is League -> LeagueMapper.mapLeague(json)
            is Team -> TeamMapper.mapTeam(json)
            is Sport -> SportMapper.mapSport(json)
        }

        return StandardResponse<T>(res)
    }
}*/