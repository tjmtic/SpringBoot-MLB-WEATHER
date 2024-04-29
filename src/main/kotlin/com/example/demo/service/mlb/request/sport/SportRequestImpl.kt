package com.example.demo.service.mlb.request.sport

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Sport
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class SportRequestImpl(val webClient: WebClient): SportRequest {

    companion object {
        const val PATH_NAME = "sports"
        const val NOT_FOUND = "Sport: Not Found"
    }

    override fun getSports(): List<Sport> {
        try {
            return getSportsRequest().block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getSportsRequest(): Mono<List<Sport?>> {

        return webClient.get()
            .uri(PATH_NAME)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Sport>().map<Sport>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getSport(id: String): Sport {
        try {
            return getSportRequest(id).block()?.first() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getSportRequest(id: String): Mono<List<Sport?>> {

        return webClient.get()
            .uri("${PATH_NAME}/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Sport>().map<Sport>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}