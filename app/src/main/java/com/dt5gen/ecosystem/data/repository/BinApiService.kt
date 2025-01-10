package com.dt5gen.ecosystem.data.repository

import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BinApiService {
    @GET("/{bin}")
    suspend fun getBinInfo(@Path("bin") bin: String): BinInfoResponse
}
