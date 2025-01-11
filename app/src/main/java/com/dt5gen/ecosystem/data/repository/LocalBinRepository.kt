package com.dt5gen.ecosystem.data.repository

import com.dt5gen.ecosystem.domain.models.BinHistoryItem

interface LocalBinRepository {
    suspend fun saveHistoryItem(item: BinHistoryItem)
    suspend fun getHistory(): List<BinHistoryItem>
    suspend fun clearHistory()
}
