package com.example.demo.data.mlb.model

data class GenericResponse<T>(val data:T, val games: List<Game>?, val venues: List<Venue>?, val team: List<Team?>?, val leagues: List<League?>?, val sport: List<Sport?>?, val divisions: List<Division?>?)