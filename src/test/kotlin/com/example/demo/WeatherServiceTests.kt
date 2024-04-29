package com.example.demo

import com.example.demo.data.weather.model.WeatherResponse
import com.example.demo.service.demo.DemoServiceImpl
import com.example.demo.service.weather.WeatherServiceException
import com.example.demo.service.weather.WeatherServiceImpl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersUriSpec
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@SpringBootTest
class WeatherServiceTests {

    private lateinit var weatherService: WeatherServiceImpl
    var mockWebServer: MockWebServer = MockWebServer()

    @BeforeEach
    fun setupMocks() {
        mockWebServer.start()

        //Enable Mocked WebServer responses
        val baseUrl = mockWebServer.url("/").toString()
        val webClient = WebClient.builder().baseUrl(baseUrl).build()

        //Inject the MockServer WebClient
        weatherService = WeatherServiceImpl(webClient)
    }

    @AfterEach
    fun `shutdown`(){
        mockWebServer.shutdown()
    }

    @Test
    fun `Test GetWeather response - FAILURE - EMPTY`(){
        // Enqueue responses
        mockWebServer.enqueue(MockResponse().setBody(""))

        try {
            val res = weatherService.getWeather(1.0, 1.0)
            fail("Expected Exception did not get thrown")
        } catch (e : WeatherServiceException){
            assertEquals(NULL_RESPONSE, e.message)
        }
    }

    @Test
    fun `Test GetWeather response - SUCCESS`(){
        // Enqueue response
        mockWebServer.enqueue(MockResponse().setBody(VALID_STRING))

        val res = weatherService.getWeather(1.0, 1.0)
        assertEquals(mockedWeatherResponse, res)
    }

    @Test
    fun `Test GetWeather response - FAILURE - INVALID`(){
        // Enqueue response
        mockWebServer.enqueue(MockResponse().setBody(INVALID_STRING))

        try {
            val res = weatherService.getWeather(1.0, 1.0)
            fail("Expected Exception did not get thrown")
        } catch (e : WeatherServiceException){
            assertEquals(INVALID_RESPONSE, e.message)
        }
    }


    companion object {
        const val VALID_STRING = "{\n" +
                "    \"@context\": [\n" +
                "        \"https://geojson.org/geojson-ld/geojson-context.jsonld\",\n" +
                "        {\n" +
                "            \"@version\": \"1.1\",\n" +
                "            \"wx\": \"https://api.weather.gov/ontology#\",\n" +
                "            \"s\": \"https://schema.org/\",\n" +
                "            \"geo\": \"http://www.opengis.net/ont/geosparql#\",\n" +
                "            \"unit\": \"http://codes.wmo.int/common/unit/\",\n" +
                "            \"@vocab\": \"https://api.weather.gov/ontology#\",\n" +
                "            \"geometry\": {\n" +
                "                \"@id\": \"s:GeoCoordinates\",\n" +
                "                \"@type\": \"geo:wktLiteral\"\n" +
                "            },\n" +
                "            \"city\": \"s:addressLocality\",\n" +
                "            \"state\": \"s:addressRegion\",\n" +
                "            \"distance\": {\n" +
                "                \"@id\": \"s:Distance\",\n" +
                "                \"@type\": \"s:QuantitativeValue\"\n" +
                "            },\n" +
                "            \"bearing\": {\n" +
                "                \"@type\": \"s:QuantitativeValue\"\n" +
                "            },\n" +
                "            \"value\": {\n" +
                "                \"@id\": \"s:value\"\n" +
                "            },\n" +
                "            \"unitCode\": {\n" +
                "                \"@id\": \"s:unitCode\",\n" +
                "                \"@type\": \"@id\"\n" +
                "            },\n" +
                "            \"forecastOffice\": {\n" +
                "                \"@type\": \"@id\"\n" +
                "            },\n" +
                "            \"forecastGridData\": {\n" +
                "                \"@type\": \"@id\"\n" +
                "            },\n" +
                "            \"publicZone\": {\n" +
                "                \"@type\": \"@id\"\n" +
                "            },\n" +
                "            \"county\": {\n" +
                "                \"@type\": \"@id\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"id\": \"https://api.weather.gov/points/40.7575,-73.8455\",\n" +
                "    \"type\": \"Feature\",\n" +
                "    \"geometry\": {\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [\n" +
                "            -73.845500000000001,\n" +
                "            40.7575\n" +
                "        ]\n" +
                "    },\n" +
                "    \"properties\": {\n" +
                "        \"@id\": \"https://api.weather.gov/points/40.7575,-73.8455\",\n" +
                "        \"@type\": \"wx:Point\",\n" +
                "        \"cwa\": \"OKX\",\n" +
                "        \"forecastOffice\": \"https://api.weather.gov/offices/OKX\",\n" +
                "        \"gridId\": \"OKX\",\n" +
                "        \"gridX\": 38,\n" +
                "        \"gridY\": 38,\n" +
                "        \"forecast\": \"https://api.weather.gov/gridpoints/OKX/38,38/forecast\",\n" +
                "        \"forecastHourly\": \"https://api.weather.gov/gridpoints/OKX/38,38/forecast/hourly\",\n" +
                "        \"forecastGridData\": \"https://api.weather.gov/gridpoints/OKX/38,38\",\n" +
                "        \"observationStations\": \"https://api.weather.gov/gridpoints/OKX/38,38/stations\",\n" +
                "        \"relativeLocation\": {\n" +
                "            \"type\": \"Feature\",\n" +
                "            \"geometry\": {\n" +
                "                \"type\": \"Point\",\n" +
                "                \"coordinates\": [\n" +
                "                    -73.749019000000004,\n" +
                "                    40.788587999999997\n" +
                "                ]\n" +
                "            },\n" +
                "            \"properties\": {\n" +
                "                \"city\": \"Harbor Hills\",\n" +
                "                \"state\": \"NY\",\n" +
                "                \"distance\": {\n" +
                "                    \"unitCode\": \"wmoUnit:m\",\n" +
                "                    \"value\": 8829.3375139906002\n" +
                "                },\n" +
                "                \"bearing\": {\n" +
                "                    \"unitCode\": \"wmoUnit:degree_(angle)\",\n" +
                "                    \"value\": 246\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"forecastZone\": \"https://api.weather.gov/zones/forecast/NYZ176\",\n" +
                "        \"county\": \"https://api.weather.gov/zones/county/NYC081\",\n" +
                "        \"fireWeatherZone\": \"https://api.weather.gov/zones/fire/NYZ212\",\n" +
                "        \"timeZone\": \"America/New_York\",\n" +
                "        \"radarStation\": \"KOKX\"\n" +
                "    }\n" +
                "}\n"
        const val INVALID_STRING = "{\"name\" : 132446562354623 }"
        const val EMPTY_STRING = ""

        const val NULL_RESPONSE = "Location: Not Found"
        const val INVALID_RESPONSE = "Location: Not Found"

        val mockedWeatherResponse = WeatherResponse(cwa="OKX", gridX="38", gridY="38", forecast="https://api.weather.gov/gridpoints/OKX/38,38/forecast")
    }
}