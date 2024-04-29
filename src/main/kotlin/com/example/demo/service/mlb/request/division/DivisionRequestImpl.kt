package com.example.demo.service.mlb.request.division

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Division
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.request.standard.StandardRequestImpl
import com.example.demo.service.mlb.request.venue.VenueRequestImpl
import jdk.jfr.ContentType
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class DivisionRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "divisions"
        const val NOT_FOUND = "Division: Not Found"
    }

    private val standardRequests = StandardRequestImpl(PATH_NAME, webClient, NOT_FOUND)

    fun getDivisions(): List<Division> {
        return standardRequests.getAll().divisions?.filterNotNull() ?: emptyList()
    }

    fun getDivision(id: String): Division? {
        return standardRequests.getById(id).divisions?.firstOrNull()
    }

    fun postDivision(id: String): Division? {
        val requestBody = mapOf(
            "id" to "string",
        )
        return webClient.post()
            .uri("divisions/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Division>().map<Division>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }.block()?.first()
    }
}