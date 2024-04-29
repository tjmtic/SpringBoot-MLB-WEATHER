package com.example.demo.service.demo

import com.example.demo.data.demo.model.GameDateResponse
import com.example.demo.data.demo.model.VenueForecastResponse
import com.example.demo.data.demo.validation.DateValidator
import com.example.demo.data.demo.validation.ValidationState
import com.example.demo.service.mlb.MlbServiceException
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.weather.WeatherServiceException
import com.example.demo.service.weather.WeatherServiceImpl
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class DemoServiceImpl(val mlbService: MlbServiceImpl, val weatherService: WeatherServiceImpl) : DemoService {

    companion object {
        const val NOT_FOUND_VENUEFORECAST = "Forecast for Venue: Not Found"
        const val NOT_FOUND_GAMEDATE = "Game Date Info: Not Found"
    }

    private val logger: Logger = LoggerFactory.getLogger(DemoService::class.java)


    override fun getForecastForVenue(id: String): VenueForecastResponse {
        logger.debug("Entering getForecastForVenue($id):")
        return try {
            mlbService.getVenue(id).let { venueResponse ->

                logger.debug("Returning getForecastForVenue($id):")

                val lat = venueResponse.result!!.venue.venueLocation.defaultCoordinates.latitude
                val lon = venueResponse.result!!.venue.venueLocation.defaultCoordinates.longitude

                VenueForecastResponse(
                    venueResponse.result!!.venue,
                    weatherService.getForecastForLocation(lat, lon)
                )
            }
        } catch (e: MlbServiceException){
            logger.error("An error occurred in getForecastForVenue method", e)
            throw DemoServiceException("${e.message}", e)
        } catch (e: WeatherServiceException){
            throw DemoServiceException("${e.message}", e)
        } catch (e: Exception){
            logger.error("An error occurred in getForecastForVenue method", e)
            throw DemoServiceException(NOT_FOUND_VENUEFORECAST, e)
        }
    }

    override fun getGamesByDate(id:String, date:String): GameDateResponse {
        logger.debug("Entering getGamesByDate($id , $date):")
        return try {
            mlbService.getVenueForGame(id, date).let{ response ->

                logger.debug("Returning getGamesByDate($id , $date):")

                val lat = response.result!!.venue.venue.venueLocation.defaultCoordinates.latitude
                val lon = response.result!!.venue.venue.venueLocation.defaultCoordinates.longitude

                GameDateResponse(date, response.result!!.game,
                    weatherService.getForecastForLocation(lat, lon)
                )
            }
        } catch (e: MlbServiceException){
            throw DemoServiceException("${e.message}", e)
        } catch (e: WeatherServiceException){
            throw DemoServiceException("${e.message}", e)
        } catch (e: Exception){
            logger.error("An error occurred in getGamesByDate method", e)
            throw DemoServiceException(NOT_FOUND_GAMEDATE, e)
        }
    }
}

class DemoServiceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
