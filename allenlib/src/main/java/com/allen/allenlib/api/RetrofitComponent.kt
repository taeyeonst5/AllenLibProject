package com.allen.allenlib.api

import android.content.Context
import com.allen.allenlib.util.logd
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * base RetrofitComponent
 * */
abstract class RetrofitComponent {
    //    private const val BASE_URL = "https://www.wecang0.com/api/"
    private val BASE_URL
        get() = getBaseUrl()

    abstract fun getBaseUrl(): String

    /**
     * return retrofitBuilder.Builder for create own ApiService Interface
     */
    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(mOkHttpClientBuilder)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    private lateinit var applicationContext: Context

    /**
     * for sharePref need applicationContext
     */
    fun setupApplicationContext(applicationContext: Context) {
        logd("application init")
        this.applicationContext = applicationContext
    }

    /***
     * can override this
     */
    open val mOkHttpClientBuilder: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * for custom Header
     */
//    private fun customHeaderInterceptor(): Interceptor {
//        return object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val request = chain.request().newBuilder()
//                    .addHeader("APPKEY", "weCan#66666666@63aF45paK98")
//                    .addHeader("authorization", getToken())
//                    .build()
//
//                val response = chain.proceed(request)
//                response.header("authorization")?.let {
//                    if (it.isEmpty()) {
//                        logd("authorization is empty")
//                    } else {
//                        logd("authorization before set:${getToken()} ///after $it")
//                        SharedPrefUtil(applicationContext).loginToken = it
//                        SharedPrefUtil(applicationContext).mid = CommonUtils.splitColon(it)
//                    }
//                }
//
//
//                return response
//            }
//
//        }
//    }
}