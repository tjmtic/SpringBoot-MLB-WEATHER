package com.example.demo.service.mlb.request.game

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Game
import com.example.demo.service.mlb.MlbServiceException
import org.springframework.web.reactive.function.client.WebClient
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
        return webClient.get()
            .uri("$PATH_NAME?scheduleTypes=games&sportIds=1&teamIds=$id&startDate=$startDate&endDate=$endDate")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Game>().map<Game>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }

    }
}