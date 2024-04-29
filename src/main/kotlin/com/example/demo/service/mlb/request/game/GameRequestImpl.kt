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

    private fun getGamesRequest(id: String, startDate: String, endDate: String,
                                scheduleTypes: String = "games", sportIds: String = "1"): Mono<List<Game?>> {

        val uriFull = UriComponentsBuilder.fromPath(PATH_NAME)
            .queryParam("scheduleTypes", scheduleTypes)
            .queryParam("sportIds", sportIds)
            .queryParam("teamIds", id)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .buildAndExpand(id)
            .toUri()

        println("REQUESTING FULL URL OF: ${uriFull}")

        //schedule?scheduleTypes=games&sportIds=1&teamIds=121&startDate=2024-03-28&endDate=2024-03-28
        //schedule?scheduleTypes=games&sportIds=1&teamIds=121&startDate=2024-03-28&endDate=2024-03-28

        return webClient.get()
            .uri("$PATH_NAME?scheduleTypes=games&sportIds=1&teamIds=$id&startDate=$startDate&endDate=$endDate")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ println("RESPONSE NO ERROR $it"); ResponseMapper<Game>().map<Game>(it) }
            .onErrorMap {
                println("ERRROR ${it.message}")
                MlbServiceException("${it.message}", it)
            }

    }
}