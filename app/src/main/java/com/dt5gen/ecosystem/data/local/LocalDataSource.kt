package com.dt5gen.ecosystem.data.local

import com.dt5gen.ecosystem.data.repository.LocalBinRepository
import com.dt5gen.ecosystem.domain.models.BinHistoryItem
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val binHistoryDao: BinHistoryDao
) : LocalBinRepository {

    override suspend fun saveHistoryItem(item: BinHistoryItem) {
        binHistoryDao.insertHistoryItem(item)
    }

    override suspend fun getHistory(): List<BinHistoryItem> {
        return binHistoryDao.getHistory()
    }

    override suspend fun clearHistory() {
        binHistoryDao.clearHistory()
    }
}
