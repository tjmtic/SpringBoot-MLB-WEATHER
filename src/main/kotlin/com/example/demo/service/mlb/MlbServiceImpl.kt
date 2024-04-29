package com.example.demo.service.mlb

import com.example.demo.controller.DemoController
import com.example.demo.data.mlb.mapper.ResponseMapper
import com.example.demo.data.mlb.model.Game
import com.example.demo.data.mlb.model.GameVenueResponse
import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.mlb.request.division.DivisionRequestImpl
import com.example.demo.service.mlb.request.game.GameRequestImpl
import com.example.demo.service.mlb.request.league.LeagueRequestImpl
import com.example.demo.service.mlb.request.sport.SportRequestImpl
import com.example.demo.service.mlb.request.team.TeamRequestImpl
import com.example.demo.service.mlb.request.venue.VenueRequestImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MlbServiceImpl(@Qualifier("MlbWebClient") final val webClient: WebClient) : MlbService {

    companion object {
        const val NOT_FOUND = "Game Venue: Not Found"
        const val UNKNOWN_EXCEPTION = "MlbService - Unknown Exception"
    }

    val leagueRequests = LeagueRequestImpl(webClient)
    val sportRequests = SportRequestImpl(webClient)
    val teamRequests = TeamRequestImpl(webClient)
    val venueRequests = VenueRequestImpl(webClient)
    val gameRequests = GameRequestImpl(webClient)
    val divisionRequests = DivisionRequestImpl(webClient)

    private val logger: Logger = LoggerFactory.getLogger(MlbServiceImpl::class.java)

    override fun getVenueForGame(id:String, date:String): GameVenueResponse {
        return try {
            gameRequests.getGames(id, date, date).let { gameResponse: List<Game> ->
                gameResponse.first().let {
                    GameVenueResponse(it, venueRequests.getVenue(it.venueId))
                }
            }
        } catch(e: MlbServiceException){
            throw MlbServiceException(NOT_FOUND + e.message, e)
        } catch(e: Exception){
            throw MlbServiceException(UNKNOWN_EXCEPTION + e.message, e)
        }
    }

}
