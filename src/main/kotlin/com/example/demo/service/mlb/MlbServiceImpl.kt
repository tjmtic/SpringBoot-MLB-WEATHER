package com.example.demo.service.mlb

import com.example.demo.controller.DemoController
import com.example.demo.data.mlb.mapper.GamesResponseMapper
import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.GameVenueResponse
import com.example.demo.data.mlb.model.GamesResponse
import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.mlb.request.league.LeagueRequestImpl
import com.example.demo.service.mlb.request.sport.SportRequestImpl
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
    private val sportRequests = SportRequestImpl(webClient)

    private val logger: Logger = LoggerFactory.getLogger(DemoController::class.java)

    fun getLeagues(): MlbServiceResponse<List<League>> {
        return leagueRequests.getLeagues()
    }

    fun getLeague(id: String): MlbServiceResponse<League> {
        return leagueRequests.getLeague(id)
    }

    fun getSports(): MlbServiceResponse<List<Sport>> {
        return sportRequests.getSports()
    }

    fun getSport(id: String): MlbServiceResponse<Sport> {
        return sportRequests.getSport(id)
    }

    override fun getTeam(id: String): MlbServiceResponse<Team> {
        try {
            return MlbServiceResponse(getTeamRequest(id).block()?.first() ?: throw MlbServiceException(NOT_FOUND_TEAM), null)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_TEAM + e.message, e)
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

    override fun getTeams(): MlbServiceResponse<List<Team>> {
        logger.debug("Entering getTeams:")
        try {
            return MlbServiceResponse(getTeamsRequest().block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND_TEAM), null)
        } catch (e: MlbServiceException){
            logger.debug("getTeams: MlbService Error", e)
            throw MlbServiceException(NOT_FOUND_TEAM + e.message, e)
        } catch (e: Exception){
            logger.debug("getTeams: Error", e)
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

    override fun getVenues(): MlbServiceResponse<List<Venue>> {
        try {
            return MlbServiceResponse(getVenuesRequest().block()?.filterNotNull() ?: throw MlbServiceException(NOT_FOUND_VENUE), null)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_VENUE, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenuesRequest(): Mono<List<Venue?>> {

        return webClient.get()
            .uri("venues/")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Venue>().map<Venue>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getVenue(id: String): MlbServiceResponse<Venue> {
        try {
            return MlbServiceResponse(getVenueRequest(id).block()?.first() ?: throw MlbServiceException(NOT_FOUND_VENUE), null)
        } catch (e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND_VENUE, e)
        } catch (e: Exception){
            throw MlbServiceException("${e.message}", e)
        }
    }

    private fun getVenueRequest(id: String): Mono<List<Venue?>> {

        return webClient.get()
            .uri("venues/$id?hydrate=location")
            .retrieve()
            .bodyToMono(String::class.java)
            .map{ ResponseMapper<Venue>().map<Venue>(it) }
            .onErrorMap {
                MlbServiceException("${it.message}", it)
            }
    }

    override fun getGames(id: String, startDate: String, endDate: String) : MlbServiceResponse<GamesResponse> {
        try {
            return MlbServiceResponse(
                getGamesRequest(id, startDate, endDate).block() ?: throw MlbServiceException(NOT_FOUND_GAME),
                null)
        } catch (e: MlbServiceException){
            //throw MlbServiceException(NOT_FOUND_GAME, e)
            return MlbServiceResponse(null, MlbServiceException(NOT_FOUND_GAME))
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

    override fun getVenueForGame(id:String, date:String): MlbServiceResponse<GameVenueResponse> {
        return getGames(id, date, date).let { gamesResponse ->
            MlbServiceResponse(
                GameVenueResponse(gamesResponse.result?.games!!.first(), getVenue(gamesResponse.result.games.first().venueId).result!!), null)
        }
    }

}
