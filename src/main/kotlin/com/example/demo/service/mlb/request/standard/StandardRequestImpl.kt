package com.example.demo.service.mlb.request.standard

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.mlb.request.league.LeagueRequest
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/*
class StandardRequestImpl<T>(val name: String, val webClient: WebClient) {

    fun getAll(): StandardResponse<T> {
        try {
            return getStandardRequest().block() ?: throw MlbServiceException(MlbServiceImpl.NOT_FOUND_LEAGUE)
        } catch (e: MlbServiceException){
            throw MlbServiceException(MlbServiceImpl.NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getStandardRequest(): Mono<StandardResponse<T>> {

        return webClient.get()
            .uri(name)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ T::map(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    fun getById(id: String): StandardResponse<T> {
        try {
            return getStandardRequest(id).block() ?: throw MlbServiceException(MlbServiceImpl.NOT_FOUND_LEAGUE)
        } catch (e: MlbServiceException){
            throw MlbServiceException(MlbServiceImpl.NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getStandardRequest(id: String): Mono<StandardResponse<T>> {

        return webClient.get()
            .uri("${name}/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<T>().map<T>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}*/

