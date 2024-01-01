package com.example.cryptoapp.persistence.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

class SharedViewModel : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun triggerError(message: String) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }


    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body()!!)
            } else {
                val errorResponse = response.errorBody()?.string()
                ApiResponse.Error(
                    errorMessage = errorResponse ?: "Unknown error occurred",
                    errorCode = response.code()
                )
            }
        } catch (e: Exception) {
            ApiResponse.Error(errorMessage = e.message ?: "Internet error occurred")
        }
    }
}