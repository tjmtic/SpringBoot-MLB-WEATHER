package com.example.demo.service.mlb.request.league

import com.example.demo.data.mlb.model.League
import com.example.demo.service.mlb.MlbServiceResponse

interface LeagueRequest {
    fun getLeagues(): MlbServiceResponse<List<League>>
    fun getLeague(id: String): MlbServiceResponse<League>
}