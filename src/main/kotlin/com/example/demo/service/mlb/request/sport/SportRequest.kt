package com.example.demo.service.mlb.request.sport

import com.example.demo.data.mlb.model.League
import com.example.demo.data.mlb.model.Sport
import com.example.demo.service.mlb.MlbServiceResponse

interface SportRequest {
    fun getSports(): MlbServiceResponse<List<Sport>>
    fun getSport(id: String): MlbServiceResponse<Sport>
}