package com.example.demo.service.mlb

import com.example.demo.controller.DemoController
import com.example.demo.data.mlb.mapper.GamesResponseMapper
import com.example.demo.data.mlb.mapper.TeamResponseMapper
import com.example.demo.data.mlb.mapper.TeamsResponseMapper
import com.example.demo.data.mlb.mapper.VenueResponseMapper
import com.example.demo.data.mlb.mapper.VenuesResponseMapper
import com.example.demo.data.mlb.model.GameVenueResponse
import com.example.demo.data.mlb.model.GamesResponse
import com.example.demo.data.mlb.model.TeamServiceResponse
import com.example.demo.data.mlb.model.TeamsResponse
import com.example.demo.data.mlb.model.VenueResponse
import com.example.demo.data.mlb.model.VenuesResponse
import com.example.demo.service.mlb.request.league.LeagueRequestImpl
import com.example.demo.service.mlb.request.league.LeaguesResponse
import com.example.demo.service.mlb.request.league.LeaguesServiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MlbServiceImpl(@Qualifier("MlbWebClient") val webClient: WebClient) : MlbService {

    companion object {
        const val NOT_FOUND_TEAM = "Team: Not Found"
        const val NOT_FOUND_VENUE = "Venue: Not Found"
        const val NOT_FOUND_GAME = "Game: Not Found"
        const val NOT_FOUND_LEAGUE = "League: Not Found"
        //const val NOT_FOUND_DIVISION = "Division: Not Found"
        //const val NOT_FOUND_SPORT = "Sport: Not Found"
    }

    private val leagueRequests = LeagueRequestImpl(webClient)

    private val logger: Logger = LoggerFactory.getLogger(DemoController::class.java)

    fun getLeagues(): LeaguesResponse {
        return leagueRequests.getLeagues()
    }

    fun getLeague(id: String): LeaguesResponse {
        return leagueRequests.getLeague(id)
    }

    override fun getTeam(id: String): TeamServiceResponse {
        try {
            return getTeamRequest(id).block() ?: throw MlbServiceException(NOT_FOUND_TEAM)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getTeamRequest(id: String): Mono<TeamServiceResponse> {

        return webClient.get()
            .uri("teams/$id")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ TeamResponseMapper.mapTeam(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getTeams(): TeamsResponse {
        logger.debug("Entering getTeams:")
        try {
            return getTeamsRequest().block() ?: throw MlbServiceException(NOT_FOUND_TEAM)
        } catch (e: MlbServiceException){
            logger.debug("getTeams: MlbService Error", e)
            throw MlbServiceException(NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            logger.debug("getTeams: Error", e)
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getTeamsRequest(): Mono<TeamsResponse> {

        println("GETTING TEAMS REQUEST")

        return webClient.get()
            .uri("teams")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ println(it);TeamsResponseMapper.mapTeams(it) }
            .onErrorMap {
                MlbServiceException("${it.message} ERROR MAP ERROR", it)
            }
    }

    override fun getVenues(): VenuesResponse {
        try {
            return getVenuesRequest().block() ?: throw MlbServiceException(NOT_FOUND_VENUE)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_VENUE, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenuesRequest(): Mono<VenuesResponse> {

        return webClient.get()
            .uri("venues/")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ VenuesResponseMapper.mapVenues(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getVenue(id: String): VenueResponse {
        try {
            return getVenueRequest(id).block() ?: throw MlbServiceException(NOT_FOUND_VENUE)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_VENUE, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenueRequest(id: String): Mono<VenueResponse> {

        return webClient.get()
            .uri("venues/$id?hydrate=location")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ VenueResponseMapper.mapVenues(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getGames(id: String, startDate: String, endDate: String) : GamesResponse {
        try {
            return getGamesRequest(id, startDate, endDate).block() ?: throw MlbServiceException(NOT_FOUND_GAME)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_GAME, e)
        } catch(e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

   private fun getGamesRequest(id: String, startDate: String, endDate: String): Mono<GamesResponse> {
        return webClient.get()
            .uri("schedule?scheduleTypes=games&sportIds=1&teamIds=$id&startDate=$startDate&endDate=$endDate")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ GamesResponseMapper.mapGames(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }

    }

    override fun getVenueForGame(id:String, date:String): GameVenueResponse {
        return getGames(id, date, date).let { gamesResponse ->
            GameVenueResponse(gamesResponse.games.first(), getVenue(gamesResponse.games.first().venueId))
        }
    }

}

class MlbServiceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
