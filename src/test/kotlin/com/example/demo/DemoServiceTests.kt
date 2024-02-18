package com.example.demo

import com.example.demo.data.demo.model.VenueForecastResponse
import com.example.demo.data.demo.model.GameDateResponse
import com.example.demo.data.mlb.model.GameVenueResponse
import com.example.demo.data.mlb.model.DefaultCoordinates
import com.example.demo.data.mlb.model.Game
import com.example.demo.data.mlb.model.Venue
import com.example.demo.data.mlb.model.VenueLocation
import com.example.demo.data.weather.model.ForecastResponse
import com.example.demo.data.weather.model.Period
import com.example.demo.service.demo.DemoServiceImpl
import com.example.demo.service.mlb.MlbServiceImpl
import com.example.demo.service.weather.WeatherServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.junit.jupiter.api.BeforeEach
import io.mockk.every
import io.mockk.mockkObject
import com.example.demo.data.demo.validation.DateValidator
import com.example.demo.data.demo.validation.ValidationState
import com.example.demo.service.demo.DemoServiceException

@SpringBootTest
class DemoServiceTests {

    //Mock Api Services
    @MockBean
    private lateinit var mlbService: MlbServiceImpl
    @MockBean
    private lateinit var weatherService: WeatherServiceImpl

    @Autowired
    private lateinit var demoService: DemoServiceImpl

    companion object {
        // Define the mocked response
        val mockedDefaultCoordinates = DefaultCoordinates(0.0, 0.0)
        val mockedVenueLocation = VenueLocation("Name", "City", "State", mockedDefaultCoordinates)
        val mockedVenue = Venue(0, "Name", "City", mockedVenueLocation)
        //val mockedVenueResponse = VenueResponse(mockedVenue)
        val mockedForecastResponse = ForecastResponse(Period(1, "", "", "", "", ""))
        val mockedVenueForecastResponse = VenueForecastResponse(mockedVenue, mockedForecastResponse)
        val mockedGame = Game("1", "home", "away")
        //val mockedGameResponse = GameResponse("1", "home", "away")
        val mockedGameVenueResponse = GameVenueResponse(mockedGame, mockedVenue)
        val mockedGameDateResponse = GameDateResponse("2022-01-02", mockedGame, mockedForecastResponse)

        const val DEFAULT_RESPONSE = "Service - Exception"
        const val VENUEFORECAST_RESPONSE = "Forecast for Venue: Not Found"
        const val GAMEDATE_RESPONSE = "Game Date Info: Not Found"
    }

    @BeforeEach
    fun setupMocks(){
        // Define the behavior of the mocked client
        Mockito.`when`(mlbService.venueRequests.getVenue("1")).thenReturn(mockedVenue, null)
        Mockito.`when`(mlbService.venueRequests.getVenue("2")).thenReturn(null)
        Mockito.`when`(weatherService.getForecastForLocation(0.0, 0.0)).thenReturn(mockedForecastResponse)
        Mockito.`when`(mlbService.getVenueForGame("1", "2022-01-02")).thenReturn(mockedGameVenueResponse)
    }

    @Test
    fun `Service Call 1 - SUCCESS`() {

        // Call the method from the service that interacts with the external API
        val result = demoService.getForecastForVenue("1")

        // Assert that the result matches the mocked response
        assertEquals(mockedVenueForecastResponse, result)
    }

    @Test
    fun `Service Call 1 - FAILURE - NULL`() {
        try {
            //Mocked NULL Response
            val result = demoService.getForecastForVenue("2")
            //Fail if exception not thrown
            fail("Expected Exception was not thrown")
        } catch (e: DemoServiceException){
            assertEquals(e.message, VENUEFORECAST_RESPONSE)
        }
    }

    @Test
    fun `Service Call 1 - FAILURE - EMPTY`() {
        try {
            val result = demoService.getForecastForVenue("")
            fail("Expected Exception was not thrown")
        } catch (e: DemoServiceException){
            assertEquals(e.message, VENUEFORECAST_RESPONSE)
        }
    }

    @Test
    fun `Service Call 1 - FAILURE - INVALID`() {
        try {
            val result = demoService.getForecastForVenue("a")
            fail("Expected Exception was not thrown")
        } catch (e: DemoServiceException){
            assertEquals(VENUEFORECAST_RESPONSE, e.message)
        }
    }

    @Test
    fun `Service Call 2 - SUCCESS`() {
        val result = demoService.getGamesByDate("1", "2022-01-02")
        assertEquals(mockedGameDateResponse, result)
    }


    @Test
    fun `Service Call 2 - FAILURE - INVALID ID`() {
        try {
            val result = demoService.getGamesByDate("2", "")
            fail("Expected Exception was not thrown")
        } catch (e: DemoServiceException){
            assertEquals(e.message, GAMEDATE_RESPONSE)
        }
    }

    @Test
    fun `Service Call 2 - FAILURE - INVALID DATE`() {
        try {
            val result = demoService.getGamesByDate("1", "")
            fail("Expected Exception was not thrown")
        } catch (e: DemoServiceException){
            assertEquals(e.message, GAMEDATE_RESPONSE)
        }
    }

}