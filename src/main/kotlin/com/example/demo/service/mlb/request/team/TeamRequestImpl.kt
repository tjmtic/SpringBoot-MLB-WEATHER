package com.example.demo.service.mlb.request.team

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Team
import com.example.demo.service.mlb.MlbServiceException
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class TeamRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "teams"
        const val NOT_FOUND = "Team: Not Found"
    }

    fun getTeam(id: String): Team {
        try {
            return getTeamRequest(id).block()?.first() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getTeamRequest(id: String): Mono<List<Team?>> {

        return webClient.get()
            .uri("$PATH_NAME/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Team>().map<Team>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    fun getTeams(): List<Team> {
        //logger.debug("Entering getTeams:")
        try {
            return getTeamsRequest().block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND)
        } catch (e: MlbServiceException){
            //logger.debug("getTeams: MlbService Error", e)
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
         //   logger.debug("getTeams: Error", e)
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getTeamsRequest(): Mono<List<Team?>> {

        return webClient.get()
            .uri("$PATH_NAME/")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Team>().map<Team>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }
}