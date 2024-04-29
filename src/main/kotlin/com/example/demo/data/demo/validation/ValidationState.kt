package com.example.demo.data.demo.validation

sealed class ValidationState {
    data class INVALID(val message: String) : ValidationState()
    data object VALID : ValidationState()

    companion object {
        fun validate(states: List<() -> ValidationState>): List<String> {
           //return states.map{ it }.filterIsInstance<INVALID>().map { it.message }
            return states.mapNotNull { it() as? INVALID }.map { it.message }
        }
    }
}