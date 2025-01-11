package com.dt5gen.ecosystem.domain.repository

import com.dt5gen.ecosystem.domain.models.BinHistoryItem
import com.dt5gen.ecosystem.domain.models.BinInfoResponse

interface BinRepository {
    suspend fun getBinInfo(bin: String): BinInfoResponse
    suspend fun saveHistoryItem(item: BinHistoryItem)
    suspend fun getHistory(): List<BinHistoryItem>
    suspend fun clearHistory()
}
