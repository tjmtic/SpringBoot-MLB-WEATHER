package com.example.demo.service.mlb.request.venue

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.mlb.MlbServiceException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

class VenueRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "venues"
        const val NOT_FOUND = "Venue: Not Found"
    }

    fun getVenues(): List<Venue> {
        try {
            return getVenuesRequest().block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND)
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

    fun getVenue(id: String): Venue {
        try {
            return getVenueRequest(id).block()?.first() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenueRequest(id: String, hydrate: String = "location"): Mono<List<Venue?>> {

        val uriFull = UriComponentsBuilder.fromPath("$PATH_NAME/$id")
            .queryParam("hydrate", hydrate)
            .buildAndExpand(id)
            .toUri()

        return webClient.get()
            .uri(uriFull)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Venue>().map<Venue>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}