package com.dt5gen.ecosystem.data.repository

import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.domain.repository.BinRepository
import javax.inject.Inject

class BinRepositoryImpl @Inject constructor(
    private val apiService: BinApiService
) : BinRepository {
    override suspend fun getBinInfo(bin: String): BinInfoResponse {
        return apiService.getBinInfo(bin)
    }
}
