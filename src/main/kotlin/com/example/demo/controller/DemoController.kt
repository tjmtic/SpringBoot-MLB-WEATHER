package com.example.demo.controller

import com.example.demo.data.demo.model.VenueForecastResponse
import com.example.demo.data.demo.model.GameDateResponse
import com.example.demo.data.demo.validation.DateValidator
import com.example.demo.data.demo.validation.ValidId
import com.example.demo.data.demo.validation.ValidationState
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.TeamServiceResponse
import com.example.demo.data.mlb.model.TeamsResponse
import com.example.demo.data.mlb.model.VenueResponse
import com.example.demo.data.mlb.model.VenuesResponse
import com.example.demo.network.TeamResponse
import com.example.demo.service.demo.DemoServiceException
import com.example.demo.service.demo.DemoServiceImpl
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.mlb.MlbServiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 *
 * A REST microservice to tell the weather at ballparks and upcoming games.
 */

@RestController
class DemoController(val demoService: DemoServiceImpl,
                     val mlbService: MlbServiceImpl,
    ) {

    companion object {
        const val ERROR_UNKNOWN = "Unknown Service Error"
    }

    private val logger: Logger = LoggerFactory.getLogger(DemoController::class.java)

    /**
     * Retrieves forecast data for a venue based on the provided ID.
     *
     * i.e.
     * ● Given a venue id in the request, returns in the response:
     * ○ The name of the venue and city
     * ○ The current temperature and conditions at the ballpark (wind speed etc)
     */
    @GetMapping("/test1/{id}")
    fun test1(@PathVariable @ValidId id: String): ResponseEntity<String> {
        return try {
            val res : VenueForecastResponse = demoService.getForecastForVenue(id)
            ResponseEntity.ok(res.toString())
        } catch (e: DemoServiceException){
            ResponseEntity.status(400).body("Service Error - ${e.message}")
        } catch (e: Exception){
            ResponseEntity.status(400).body(ERROR_UNKNOWN)
        }
    }

    /**
     * Retrieves game data for a venue based on the provided ID, date, and optional number of days in the future.
     *
     * i.e.
     * ● Given a baseball team id and a game date in the request, returns in the response:
     * ○ The name of the teams playing
     * ○ The date sent in the request
     * ○ The current temperature and conditions of the upcoming game (wind
     * speed etc)
     * ○ If the date in the request is in the past or is more than 7 days in the future,
     * the response should be an Invalid request. The error message in the
     * response should explain why the request is invalid.
     */
    @GetMapping("/test2/{id}")
    fun test2(@PathVariable @ValidId id: String, @RequestParam date: String, @RequestParam daysInFuture: Int = 7): ResponseEntity<String> {
        return try {
            //TODO: Abstract out (List<PropertyValidator> -> ValidationState)
            when(val state: ValidationState = DateValidator.validateDateForTimespan(date, daysInFuture)){
                is ValidationState.VALID -> {
                    val res : GameDateResponse = demoService.getGamesByDate(id, date)
                    ResponseEntity.ok(res.toString())
                }
                is ValidationState.INVALID -> {
                    ResponseEntity.ok("Invalid Date - ${state.message}")
                }
            }
           } catch (e: DemoServiceException){
            ResponseEntity.status(400).body("Service Error - ${e.message}")
        } catch (e: Exception){
            ResponseEntity.status(400).body(ERROR_UNKNOWN)
        }
    }

    @GetMapping("/getVenues/")
    fun getVenues(): ResponseEntity<String> {
        return try {
            val res : MlbServiceResponse<VenuesResponse> = mlbService.getVenues()
            ResponseEntity.ok(res.toString())
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body("Service Error - ${e.message}")
        } catch (e: Exception){
            ResponseEntity.status(400).body(ERROR_UNKNOWN)
        }
    }

    @GetMapping("/getVenue/{id}")
    fun getVenue(@PathVariable @ValidId id: String): ResponseEntity<String> {
        return try {
            val res : MlbServiceResponse<VenueResponse> = mlbService.getVenue(id)
            ResponseEntity.ok(res.toString())
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body("Service Error - ${e.message}")
        } catch (e: Exception){
            ResponseEntity.status(400).body(ERROR_UNKNOWN)
        }
    }

    @GetMapping("/getTeams/")
    fun getTeams(): ResponseEntity<String> {
        logger.debug("Entering getTeams:")
        return try {
            val res : MlbServiceResponse<TeamsResponse> = mlbService.getTeams()
            logger.debug(":Exiting getTeams.")
            ResponseEntity.ok(res.result.toString())
        } catch (e: MlbServiceException){
            logger.debug("MlbService Error:", e)
            ResponseEntity.status(400).body("Service Error - ${e.message}")
        } catch (e: Exception){
            logger.debug("Error:", e)
            ResponseEntity.status(400).body(ERROR_UNKNOWN)
        }
    }

    @GetMapping("/getTeam/{id}")
    fun getTeam(@PathVariable @ValidId id: String): ResponseEntity<TeamResponse> {
        return try {
            val res : MlbServiceResponse<TeamServiceResponse> = mlbService.getTeam(id)
            ResponseEntity.ok(TeamResponse(res.result!!.team, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(TeamResponse(null, "Service Error - ${e.message}"))
        } catch (e: Exception){
            ResponseEntity.status(400).body(TeamResponse(null, ERROR_UNKNOWN))
        }
    }
}