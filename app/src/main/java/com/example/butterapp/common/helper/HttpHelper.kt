package com.example.butterapp.common.helper

import com.example.butterapp.data.remote.BaseResponseDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

class HttpHelper {
    companion object {
        fun <T> parseResponseBody(body: Any?): T? {
            val type = object : TypeToken<T>() {}.type
            var errorResponse: T? = null
            val gson = Gson()
            try {
                if (body is ResponseBody) {
                    errorResponse = gson.fromJson(body.charStream(), type)
                }
            } catch (_: Exception) {
            }

            return errorResponse
        }
    }
}