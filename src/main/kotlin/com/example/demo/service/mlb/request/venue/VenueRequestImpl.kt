package com.example.demo.service.mlb.request.venue

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.mlb.MlbServiceResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class VenueRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "Venues"
        const val NOT_FOUND = "Venue: Not Found"
    }

    fun getVenues(): MlbServiceResponse<List<Venue>> {
        try {
            return MlbServiceResponse(getVenuesRequest().block()?.filterNotNull() ?: throw MlbServiceException(
                NOT_FOUND
            ), null)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenuesRequest(): Mono<List<Venue?>> {

        return webClient.get()
            .uri("$PATH_NAME/")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Venue>().map<Venue>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    fun getVenue(id: String): MlbServiceResponse<Venue> {
        try {
            return MlbServiceResponse(getVenueRequest(id).block()?.first() ?: throw MlbServiceException(
                NOT_FOUND
            ), null)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenueRequest(id: String): Mono<List<Venue?>> {

        return webClient.get()
            .uri("$PATH_NAME/$id?hydrate=location")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Venue>().map<Venue>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}