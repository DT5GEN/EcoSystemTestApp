package com.dt5gen.ecosystem.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dt5gen.ecosystem.domain.models.BinHistoryItem

@Dao
interface BinHistoryDao {
    @Insert
    suspend fun insertHistoryItem(item: BinHistoryItem)

    @Query("SELECT * FROM bin_history ORDER BY id DESC")
    suspend fun getHistory(): List<BinHistoryItem>

    @Query("DELETE FROM bin_history")
    suspend fun clearHistory()
}
