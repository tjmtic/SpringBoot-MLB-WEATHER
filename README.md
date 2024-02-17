#### MLB-WEATHER HOMEWORK - Timothy McArdle - February 2024

## Local Build Setup: localhost:8080
./gradlew bootRun
# Default Port and URLs included in application.properties

## build/libs for JAR
demo-0.0.1-SNAPSHOT.jar 

### HOMEWORK ENDPOINTS ###

## URL: /test1/{id}
 EXAMPLE URL: localhost:8080/test1/17
 EXAMPLE RESPONSE: Venue Forecast Info: Chicago Wrigley Field 0 to 5 mph

## URL: /test2/{id}?date={date}
 EXAMPLE URL: localhost:8080/test2/121?date=2024-03-28&daysInFuture=60
 EXAMPLE RESPONSE: Game Date Info: 2024-03-28 New York Mets Milwaukee Brewers 28F
# Optional {daysInFuture} argument, extensible parameter
# Offseason Convenience Option


###
* Kotlin
* Spring Boot
* Gradle
* Microservice
* REST
* Layered Services
* Smart Endpoints / Dumb Pipes
* CLEAN
* Dependency Injection
* Build Env Vars
* Data Validation - Annotated, Custom
* Data Transformation Objects

* Controller layer Testing
* Service layer Testing
* Network (API) layer Testing
* Data layer Testing
* Validation testing
* Mocked Services and Data

## Future Work - Service, Validation, and Data layer Testing Code Coverage; CLI Testing; 
