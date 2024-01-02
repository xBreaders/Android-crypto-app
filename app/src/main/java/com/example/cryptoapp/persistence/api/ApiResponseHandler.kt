package com.example.cryptoapp.persistence.api

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

/**
 * ViewModel class that exposes shared state and handles interactions with API calls.
 *
 * This class exposes error messages as a StateFlow for observing and handles triggering and clearing of errors.
 * Furthermore, it provides a method for safely conducting API calls and categorizing the response as either Success or Error.
 */
class SharedViewModel : ViewModel() {
    /**
     * A private mutable state flow of error message.
     */
    private val _errorMessage = MutableStateFlow<String?>(null)

    /**
     * Public exposure of _errorMessage as StateFlow (read-only) error message.
     */
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Function to send a new error message value to _errorMessage state flow.
     *
     * @param message The error message to be sent as state
     */
    fun triggerError(message: String) {
        _errorMessage.value = message
    }

    /**
     * Function to clear the error message value by setting it to null.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Function to execute API calls safely and capturing the success or error response,
     * wrapping it in ApiResponse type.
     *
     * @param call The API call to be executed that returns a Response of type [T].
     *
     * @return An ApiResponse object that can be ApiResponse.Success if the API call was successful, or
     * ApiResponse.Error in case of an error.
     */
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body()!!)
            } else {
                val errorResponse = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorResponse, ApiErrorResponse::class.java)
                ApiResponse.Error(
                    errorMessage = "Error ${apiError.status.error_code}: ${apiError.status.error_message}",
                    errorCode = apiError.status.error_code
                )
            }
        } catch (e: Exception) {
            ApiResponse.Error(errorMessage = e.message ?: "Internet error occurred")
        }
    }
}