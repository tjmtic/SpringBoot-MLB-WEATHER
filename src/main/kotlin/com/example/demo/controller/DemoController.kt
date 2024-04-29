package com.example.demo.controller

import com.example.demo.data.demo.model.VenueForecastResponse
import com.example.demo.data.demo.model.GameDateResponse
import com.example.demo.data.demo.validation.DateValidator
import com.example.demo.data.demo.validation.ValidId
import com.example.demo.data.demo.validation.ValidationState
import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.data.mlb.model.Team
import com.example.demo.data.mlb.model.Venue
import com.example.demo.service.demo.DemoServiceException
import com.example.demo.service.demo.DemoServiceImpl
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
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
            //Implement Validators List
            val dateValidator = { DateValidator.validateDateForTimespan(date, daysInFuture) }
            val listOfValidators = listOf(dateValidator)

            when(val state: List<String> = ValidationState.validate(listOfValidators)){
                //No Validation Error Messages, good to go forward
                emptyList<String>() -> {
                    val res : GameDateResponse = demoService.getGamesByDate(id, date)
                    ResponseEntity.ok(res.toString())
                }
                //else send back validation errors
                else -> {
                    val messages = state.map { it }
                    ResponseEntity.ok("Invalid Request - ${messages}")
                }
            }
           } catch (e: DemoServiceException){
            ResponseEntity.status(400).body("Service Error - ${e.message}")
        } catch (e: Exception){
            ResponseEntity.status(400).body(ERROR_UNKNOWN)
        }
    }

    /*private fun validate(states: List<() -> ValidationState>): List<String>{
        return states.map{ it() }.filterIsInstance<ValidationState.INVALID>().map { it.message }
    }*/

    @GetMapping("/getVenues/")
    fun getVenues(): ResponseEntity<ServiceResponse<List<Venue>>> {
        return try {
            val res : List<Venue> = mlbService.venueRequests.getVenues()
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getVenue/{id}")
    fun getVenue(@PathVariable @ValidId id: String): ResponseEntity<ServiceResponse<Venue>> {
        return try {
            val res : Venue = mlbService.venueRequests.getVenue(id)
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getTeams/")
    fun getTeams(): ResponseEntity<ServiceResponse<List<Team>>> {
        logger.debug("Entering getTeams:")
        return try {
            val res : List<Team> = mlbService.teamRequests.getTeams()
            logger.debug(":Exiting getTeams.")
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            logger.debug("MlbService Error:", e)
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            logger.debug("Error:", e)
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getTeam/{id}")
    fun getTeam(@PathVariable @ValidId id: String): ResponseEntity<ServiceResponse<Team>> {
        return try {
            val res : Team = mlbService.teamRequests.getTeam(id)
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getLeague/{id}")
    fun getLeague(@PathVariable @ValidId id: String): ResponseEntity<ServiceResponse<League>> {
        return try {
            val res : League = mlbService.leagueRequests.getLeague(id)
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getLeagues/")
    fun getLeagues(): ResponseEntity<ServiceResponse<List<League>>> {
        return try {
            val res : List<League> = mlbService.leagueRequests.getLeagues()
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getSport/{id}")
    fun getSport(@PathVariable @ValidId id: String): ResponseEntity<ServiceResponse<Sport>> {
        return try {
            val res : Sport = mlbService.sportRequests.getSport(id)
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }

    @GetMapping("/getSports/")
    fun getSports(): ResponseEntity<ServiceResponse<List<Sport>>> {
        return try {
            val res : List<Sport> = mlbService.sportRequests.getSports()
            ResponseEntity.ok(ServiceResponse(res, null))
        } catch (e: MlbServiceException){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException("Service Error - ${e.message}")))
        } catch (e: Exception){
            ResponseEntity.status(400).body(ServiceResponse(null, ServiceException(ERROR_UNKNOWN)))
        }
    }
}

data class ServiceResponse<T>(val result: T?, val error: ServiceException?)
class ServiceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
