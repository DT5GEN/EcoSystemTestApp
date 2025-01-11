package com.dt5gen.ecosystem.data.repository

import com.dt5gen.ecosystem.domain.models.BinHistoryItem
import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.domain.models.toHistoryItem
import com.dt5gen.ecosystem.domain.repository.BinRepository
import javax.inject.Inject

class BinRepositoryImpl @Inject constructor(
    private val apiService: BinApiService,
    private val localBinRepository: LocalBinRepository
) : BinRepository {

    override suspend fun getBinInfo(bin: String): BinInfoResponse {
        return try {
            val response = apiService.getBinInfo(bin)
            localBinRepository.saveHistoryItem(response.toHistoryItem(bin))
            response
        } catch (e: Exception) {
            throw Exception("Ошибка загрузки BIN информации")
        }
    }

    override suspend fun getHistory(): List<BinHistoryItem> {
        return localBinRepository.getHistory()
    }

    override suspend fun saveHistoryItem(item: BinHistoryItem) {
        localBinRepository.saveHistoryItem(item)
    }

    override suspend fun clearHistory() {
        localBinRepository.clearHistory()
    }
}
