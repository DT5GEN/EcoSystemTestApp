package com.dt5gen.ecosystem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dt5gen.ecosystem.domain.models.BinHistoryItem

@Database(entities = [BinHistoryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun binHistoryDao(): BinHistoryDao
}
