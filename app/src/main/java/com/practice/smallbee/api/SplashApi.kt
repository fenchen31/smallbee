package com.practice.smallbee.api

import com.practice.common.service.ApiResponse
import com.practice.smallbee.response.SplashAdvertiseResponse
import retrofit2.Response
import retrofit2.http.GET

interface SplashApi {
    @GET()
    suspend fun getAdvertise(): Response<ApiResponse<SplashAdvertiseResponse>>
}