package com.example.demo.service.mlb.request.division

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Division
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.request.standard.StandardRequestImpl
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class DivisionRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "division"
        const val NOT_FOUND = "Division: Not Found"
    }

    private val standardRequests = StandardRequestImpl(PATH_NAME, webClient, NOT_FOUND)

    fun getDivisions(): List<Division> {
        return standardRequests.getAll().divisions?.filterNotNull() ?: emptyList()
    }

    fun getDivision(id: String): Division? {
        return standardRequests.getById(id).divisions?.firstOrNull()
    }
}