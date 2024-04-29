package com.example.demo.service.fb

import com.example.demo.data.weather.mapper.ForecastResponseMapper
import com.example.demo.data.weather.mapper.WeatherResponseMapper
import com.example.demo.data.weather.model.ForecastResponse
import com.example.demo.data.weather.model.WeatherResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.serializer.support.SerializationFailedException
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class FbServiceImpl(@Qualifier("FbWebClient") val webClient: WebClient): FbService {

    companion object {
        const val NOT_FOUND = "Fb: Not Found"
    }


    //TODO: Should come from FB LOGIN action
    @Autowired
    private lateinit var FB_ID: String
    @Autowired
    private lateinit var FB_TOKEN: String

    internal fun postIg(imageUrl: String, caption: String): String {
        return try {
            postIgRequest(imageUrl, caption).block() ?: throw FbServiceException(NOT_FOUND)
        } catch (e: FbServiceException){
            throw FbServiceException(NOT_FOUND, e)
        } catch (e: Exception){
            throw FbServiceException("${e.message}", e)
        }
    }

    private fun postIgRequest(imageUrl: String, caption: String): Mono<String> {
        val requestBody = mapOf(
            "image_url" to imageUrl,
            "is_carousel_item" to "false",
            "caption" to caption,
            "access_token" to FB_TOKEN
        )


        return webClient.post()
            .uri("$FB_ID/media")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(requestBody.toString())
            .retrieve()
            .bodyToMono(String::class.java)
           // .map{ WeatherResponseMapper.mapWeather(it) }
            .onErrorMap {
                FbServiceException("${it.message}", it)
            }
    }

    internal fun publishIg(postId: String): String {
        return try {
            publishIgRequest(postId).block() ?: throw FbServiceException(NOT_FOUND)
        } catch (e: FbServiceException){
            throw FbServiceException(NOT_FOUND, e)
        } catch (e: Exception){
            throw FbServiceException("${e.message}", e)
        }
    }

    private fun publishIgRequest(postId: String): Mono<String> {
        val requestBody = mapOf(
            "creation_id" to postId,
            "access_token" to FB_TOKEN
        )


        return webClient.post()
            .uri("$FB_ID/media_publish")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(requestBody.toString())
            .retrieve()
            .bodyToMono(String::class.java)
            // .map{ WeatherResponseMapper.mapWeather(it) }
            .onErrorMap {
                FbServiceException("${it.message}", it)
            }
    }

    override fun postIgItem(imageUrl: String, caption: String): String {
        return postIg(imageUrl, caption).let {
            publishIg(it)
        }
    }
}

class FbServiceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)