package com.allen.allenlib.api

import okhttp3.Interceptor
import okhttp3.Response

class CustomHeaderInterceptor(private val headers: Map<String, String>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        headers.forEach {
            request.addHeader(it.key, it.value)
        }
        return chain.proceed(request.build())
    }

}