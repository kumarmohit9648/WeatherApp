package com.mohit.weathersdk.network.repo

import android.content.Context
import com.google.gson.Gson

import com.mohit.weathersdk.util.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRepository @Inject constructor(open val context: Context) {

    @Inject
    lateinit var gson: dagger.Lazy<Gson>

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
        return extractResultResource(call)
    }

    private suspend fun <T> extractResultResource(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                val errBodyString = response.errorBody()?.string()
                val errorResponse = gson.get().fromJson(errBodyString, com.mohit.weathersdk.network.models.ErrorResponse::class.java)
                val errorMessage = parseErrorMessage(errorResponse)
                // Log the error message.
                println("BaseRepository: FAIL $errBodyString \n $errorMessage")
                Resource.error(msg = errorMessage)
            }
        } catch (e: Throwable) {
            val errorResponse = parseErrorResponse(e)
            val errorMessage = parseErrorMessage(errorResponse)
            val errMsg = when (e) {
                is HttpException -> errorMessage
                is ConnectException -> "No Internet."
                is UnknownHostException -> "No host available."
                is SocketTimeoutException -> "Socket time-out."
                else -> "Unexpected Error."
            }
            // Log the error message.
            println("BaseRepository: THROWABLE ${errorResponse.toString()} \n" +
                    " $errorMessage")
            Resource.error(msg = errMsg)
        }
    }

    private fun parseErrorResponse(throwable: Throwable?): com.mohit.weathersdk.network.models.ErrorResponse? {
        var errorResponse: com.mohit.weathersdk.network.models.ErrorResponse? = null
        throwable?.let {
            if (it is HttpException) {
                val responseBodyJson = it.response()?.errorBody()?.string()
                errorResponse =  gson.get().fromJson(responseBodyJson, com.mohit.weathersdk.network.models.ErrorResponse::class.java)
            }
        }
        return errorResponse
    }

    private fun parseErrorMessage(errorResponse: com.mohit.weathersdk.network.models.ErrorResponse?): String {
        var message = "Unexpected Error."
        errorResponse?.error?.let {
            when (it.code) {
                503 -> {
                    message = "Unexpected Error."
                }
                else -> {
                    message = it.message ?: "Unexpected Error."
                }
            }
        }
        return message
    }

}