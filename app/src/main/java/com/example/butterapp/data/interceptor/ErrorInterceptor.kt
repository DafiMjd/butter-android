package com.example.butterapp.data.interceptor

import com.example.butterapp.data.remote.BaseResponseDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val isError = !response.isSuccessful

        if (isError) {
            val type = object : TypeToken<BaseResponseDto<Unit>>() {}.type
            var errorResponse: BaseResponseDto<Unit>? = null
            val gson = Gson()
            try {
                errorResponse =  gson.fromJson(response.body()?.charStream(), type)
            } catch(e: Exception) {
                e.printStackTrace()
            }

            return Response.Builder()
                .request(chain.request())
                .protocol(response.protocol())
                .code(response.code())
                .message(errorResponse?.message ?: response.message())
                .body(response.body())
                .build()
        }

        return response
    }
}