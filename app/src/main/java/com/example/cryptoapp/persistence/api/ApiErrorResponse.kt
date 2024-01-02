package com.example.cryptoapp.persistence.api

data class ApiErrorResponse(
    val status: Status
) {
    data class Status(
        val timestamp: String,
        val error_code: Int,
        val error_message: String,
        val elapsed: Int,
        val credit_count: Int
    )
}