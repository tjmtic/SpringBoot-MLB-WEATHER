package com.example.demo.service.mlb.request.standard

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.mapper.StandardResponseMapper
import com.example.demo.data.mlb.model.GenericResponse
import com.example.demo.data.mlb.model.StandardMapping
import com.example.demo.data.mlb.model.StandardResponse
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.mlb.request.league.LeagueRequest
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


class StandardRequestImpl(val name: String, val webClient: WebClient, val error: String) {

    companion object {
        const val NOT_FOUND = "Request: Not Found"
    }

    fun getAll(): StandardResponse {
        try {
            return getStandardRequest().block() ?: throw MlbServiceException(error)
        } catch (e: MlbServiceException){
            throw MlbServiceException(error + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getStandardRequest(): Mono<StandardResponse> {

        return webClient.get()
            .uri(name)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ StandardResponseMapper().map(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    fun getById(id: String): StandardResponse {
        try {
            return getStandardRequest(id).block() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    /*private fun <T> getStandardRequest(id: String): Mono<List<T?>?> {

        return webClient.get()
            .uri("${name}/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<T>().map<T>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }*/

    private fun getStandardRequest(id: String): Mono<StandardResponse> {

        return webClient.get()
            .uri("${name}/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ StandardResponseMapper().map(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    fun getCustom(urlPath: String): StandardResponse {
        try {
            return getStandardRequestCustom(urlPath).block() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getStandardRequestCustom(urlPath: String): Mono<StandardResponse> {

        return webClient.get()
            .uri(urlPath)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ StandardResponseMapper().map(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}

