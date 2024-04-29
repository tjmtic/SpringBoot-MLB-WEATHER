package com.example.demo

import com.example.demo.controller.DemoController
import com.example.demo.data.demo.model.GameDateResponse
import com.example.demo.data.demo.model.VenueForecastResponse
import com.example.demo.data.demo.validation.DateValidator
import com.example.demo.data.mlb.model.DefaultCoordinates
import com.example.demo.data.mlb.model.Game
import com.example.demo.data.mlb.model.GameResponse
import com.example.demo.data.mlb.model.GameVenueResponse
import com.example.demo.data.mlb.model.Venue
import com.example.demo.data.mlb.model.VenueLocation
import com.example.demo.data.mlb.model.VenueResponse
import com.example.demo.data.weather.model.ForecastResponse
import com.example.demo.data.weather.model.Period
import com.example.demo.service.demo.DemoServiceImpl
import com.example.demo.service.mlb.MlbServiceImpl
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(DemoController::class)
class DemoControllerTests {

	@MockBean
	private lateinit var demoService: DemoServiceImpl
	@MockBean
	private lateinit var mlbService: MlbServiceImpl

	@Autowired
	private lateinit var mockMvc: MockMvc

	private lateinit var demoController: DemoController


	companion object {
		const val SUCCESS_VENUEFORECAST = "Venue Forecast Info: City Name 0"
		const val SUCCESS_GAMEDATE = "Game Date Info: 2022-01-02 home away TempUn"
		const val ERROR_DATE_PAST = "Invalid Date - Day in the Past"
		const val ERROR_DATE_FUTURE = "Invalid Date - Day in the Future"

		val mockedDefaultCoordinates = DefaultCoordinates(0.0, 0.0)
		val mockedVenueLocation = VenueLocation("Name", "City", "State", mockedDefaultCoordinates)
		val mockedVenue = Venue(0, "Name", "City", mockedVenueLocation)
		val mockedVenueResponse = VenueResponse(mockedVenue)
		val mockedForecastResponse = ForecastResponse(Period(1, "Temp", "Un", "0", "N", "Short Forecast"))
		val mockedVenueForecastResponse = VenueForecastResponse(mockedVenue, mockedForecastResponse)
		val mockedGame = Game("1", "home", "away")
		val mockedGameResponse = GameResponse("1", "home", "away")
		val mockedGameVenueResponse = GameVenueResponse(mockedGame, mockedVenueResponse)
		val mockedGameDateResponse = GameDateResponse("2022-01-02", mockedGame, mockedForecastResponse)
	}


	@BeforeEach
	fun setupMocks(){
		// Mock the companion object
		mockkObject(DateValidator)
		every { DateValidator.getCurrentDateAsString("yyyy-MM-dd") } returns "2022-01-02"

		//Mock the Service calls
		Mockito.`when`(demoService.getForecastForVenue("12")).thenReturn(mockedVenueForecastResponse)
		Mockito.`when`(demoService.getGamesByDate("12","2022-01-02")).thenReturn(mockedGameDateResponse)

		demoController = DemoController(demoService, mlbService)
	}


	@Test
	fun `Test Get VenueForecast Info - SUCCESS`() {
		mockMvc.perform(get("/test1/12"))
			.andExpect(status().isOk)
			.andExpect(content().string(SUCCESS_VENUEFORECAST))
	}

	@Test
	fun `Test Get VenueForecast Info - FAILURE - ID - 1`() {
		mockMvc.perform(get("/test1/a"))
			.andExpect(status().is4xxClientError)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `Test Get VenueForecast Info - FAILURE - ID - 2`() {
		mockMvc.perform(get("/test1/10000"))
			.andExpect(status().is4xxClientError)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `Test Get GameDate Info 2 - SUCCESS`() {
		mockMvc.perform(get("/test2/12?date=2022-01-02"))
			.andExpect(status().isOk)
			.andExpect(content().string(SUCCESS_GAMEDATE))
	}

	@Test
	fun `Test Get GameDate Info 2 - SUCCESS - 1`() {
		mockMvc.perform(get("/test2/12?date=2022-01-02"))
			.andExpect(status().isOk)
			.andExpect(content().string(SUCCESS_GAMEDATE))
	}

	@Test
	fun `Test Get GameDate Info 2 - FAILURE - ID`() {
		mockMvc.perform(get("/test2/a?date=2021-01-02"))
			.andExpect(status().is4xxClientError)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `Test Get GameDate Info 2 - FAILURE - DATE - PAST`() {
		mockMvc.perform(get("/test2/12?date=2021-01-02"))
			.andExpect(status().isOk)
			.andExpect(content().string(ERROR_DATE_PAST))
	}

	@Test
	fun `Test Get GameDate Info 2 - FAILURE - DATE - FUTURE`() {
		mockMvc.perform(get("/test2/12?date=2022-02-02"))
			.andExpect(status().isOk)
			.andExpect(content().string(ERROR_DATE_FUTURE))
	}

}
