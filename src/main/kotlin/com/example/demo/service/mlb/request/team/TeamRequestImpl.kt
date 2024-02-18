package com.example.demo.service.mlb.request.team

import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Team
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.mlb.MlbServiceResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class TeamRequestImpl(val webClient: WebClient) {

    companion object {
        const val PATH_NAME = "teams"
        const val NOT_FOUND = "Team: Not Found"
    }

    fun getTeam(id: String): MlbServiceResponse<Team> {
        try {
            return MlbServiceResponse(getTeamRequest(id).block()?.first() ?: throw MlbServiceException(
                NOT_FOUND
            ), null)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getTeamRequest(id: String): Mono<List<Team?>> {

        return webClient.get()
            .uri("teams/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Team>().map<Team>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    fun getTeams(): MlbServiceResponse<List<Team>> {
        //logger.debug("Entering getTeams:")
        try {
            return MlbServiceResponse(getTeamsRequest().block()?.filterNotNull() ?: throw MlbServiceException(
                NOT_FOUND
            ), null)
        } catch (e: MlbServiceException){
            //logger.debug("getTeams: MlbService Error", e)
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch (e: Exception){
         //   logger.debug("getTeams: Error", e)
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getTeamsRequest(): Mono<List<Team?>> {

        println("GETTING TEAMS REQUEST")

        return webClient.get()
            .uri("teams")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Team>().map<Team>(it) }
            .onErrorMap {
                MlbServiceException("${it.message} ERROR MAP ERROR", it)
            }
    }
}