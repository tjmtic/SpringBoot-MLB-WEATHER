package com.example.demo.service.mlb.request.league

interface LeagueRequest {
    fun getLeagues(): LeaguesResponse
    fun getLeague(id: String): LeaguesResponse
}