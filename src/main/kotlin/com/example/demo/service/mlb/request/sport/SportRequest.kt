package com.example.demo.service.mlb.request.sport

import com.example.demo.data.mlb.model.Sport

interface SportRequest {
    fun getSports(): List<Sport>
    fun getSport(id: String): Sport
}