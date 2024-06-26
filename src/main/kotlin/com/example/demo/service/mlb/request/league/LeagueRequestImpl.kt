package com.example.demo.service.mlb.request.league

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.League
import com.example.demo.service.mlb.MlbServiceException
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class LeagueRequestImpl(val webClient: WebClient): LeagueRequest {

    companion object {
        const val PATH_NAME = "league"
        const val NOT_FOUND = "League: Not Found"
    }

    override fun getLeagues(): List<League> {
        try {
            return getLeaguesRequest().block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getLeaguesRequest(): Mono<List<League?>> {

        return webClient.get()
            .uri(PATH_NAME)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<League>().map<League>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getLeague(id: String): League {
        try {
            return getLeagueRequest(id).block()?.first() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getLeagueRequest(id: String): Mono<List<League?>> {

        return webClient.get()
            .uri("${PATH_NAME}/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<League>().map<League>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}