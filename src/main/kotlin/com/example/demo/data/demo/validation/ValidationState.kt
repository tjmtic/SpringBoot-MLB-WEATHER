package com.example.demo.data.demo.validation

sealed class ValidationState {
    data class INVALID(val message: String) : ValidationState()
    data object VALID : ValidationState()
}