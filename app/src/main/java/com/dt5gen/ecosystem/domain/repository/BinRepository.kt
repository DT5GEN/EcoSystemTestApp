package com.dt5gen.ecosystem.domain.repository

import com.dt5gen.ecosystem.domain.models.BinInfoResponse

interface BinRepository {
    suspend fun getBinInfo(bin: String): BinInfoResponse
}
