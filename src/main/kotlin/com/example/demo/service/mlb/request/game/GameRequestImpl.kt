package com.example.demo.service.mlb.request.game

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Game
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.request.venue.VenueRequestImpl
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

class GameRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "schedule"
        const val NOT_FOUND = "Game: Not Found"
    }
    fun getGames(id: String, startDate: String, endDate: String) : List<Game> {
        return try {
                getGamesRequest(id, startDate, endDate).block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND, e)
            //MlbServiceResponse(null, MlbServiceException(NOT_FOUND))
        } catch(e: Exception){
            throw MlbServiceException("${e.message}", e)
            //MlbServiceResponse(null, MlbServiceException("${e.message}", e))
        }
    }

    private fun getGamesRequest(id: String, startDate: String, endDate: String): Mono<List<Game?>> {

        val uriFull = UriComponentsBuilder.fromPath(PATH_NAME)
            .queryParam("scheduleTypes", "games")
            .queryParam("sportIds", "1")
            .queryParam("teamIds", id)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .buildAndExpand(id)
            .toUri()

        return webClient.get()
            .uri(uriFull)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Game>().map<Game>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }

    }
}