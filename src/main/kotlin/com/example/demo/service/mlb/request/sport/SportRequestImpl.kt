package com.example.demo.service.mlb.request.sport

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Sport
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.mlb.MlbServiceResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class SportRequestImpl(val webClient: WebClient): SportRequest {

    companion object {
        const val PATH_NAME = "sport"
        const val NOT_FOUND = "Sport: Not Found"
    }

    override fun getSports(): MlbServiceResponse<List<Sport>> {
        try {
            return getSportsRequest().block() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(MlbServiceImpl.NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getSportsRequest(): Mono<MlbServiceResponse<List<Sport>>> {

        return webClient.get()
            .uri(PATH_NAME)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<List<Sport>>().map<List<Sport>>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getSport(id: String): MlbServiceResponse<Sport> {
        try {
            return getSportRequest(id).block() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(MlbServiceImpl.NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getSportRequest(id: String): Mono<MlbServiceResponse<Sport>> {

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