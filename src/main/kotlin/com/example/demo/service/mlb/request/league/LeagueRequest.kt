package com.example.demo.service.mlb.request.league

import com.example.demo.data.mlb.model.League

interface LeagueRequest {
    fun getLeagues(): List<League>
    fun getLeague(id: String): League
}