package com.example.demo.service.mlb.request.league

import com.example.demo.data.mlb.mapper.LeaguesResponseMapper
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class LeagueRequestImpl(val webClient: WebClient): LeagueRequest {

    override fun getLeagues(): LeaguesResponse {
        try {
            return getLeaguesRequest().block() ?: throw MlbServiceException(MlbServiceImpl.NOT_FOUND_LEAGUE)
        } catch (e: MlbServiceException){
            throw MlbServiceException(MlbServiceImpl.NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getLeaguesRequest(): Mono<LeaguesResponse> {

        return webClient.get()
            .uri("league")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ LeaguesResponseMapper.mapLeagues(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getLeague(id: String): LeaguesResponse {
        try {
            return getLeagueRequest(id).block() ?: throw MlbServiceException(MlbServiceImpl.NOT_FOUND_LEAGUE)
        } catch (e: MlbServiceException){
            throw MlbServiceException(MlbServiceImpl.NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getLeagueRequest(id: String): Mono<LeaguesResponse> {

        return webClient.get()
            .uri("league/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ LeaguesResponseMapper.mapLeagues(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}