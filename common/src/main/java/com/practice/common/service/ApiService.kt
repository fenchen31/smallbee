package com.practice.common.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.concurrent.Volatile

class ApiService private constructor(){

     companion object {
        private const val BASE_URL = "https://www.baidu.com/"
        private const val CONNECTION_TIMOUT = 20L
        private const val READ_TIMOUT = 20L
        private const val WRITE_TIMOUT = 20L
        @Volatile
        private var instance: ApiService? = null

        fun getInstance(): ApiService {
            if (instance == null) {
                synchronized(ApiService::class.java) {
                    if (instance == null) {
                        instance = ApiService()
                    }
                }
            }
            return instance!!
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECTION_TIMOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}