package com.dt5gen.ecosystem.di

import android.content.Context
import androidx.room.Room
import com.dt5gen.ecosystem.data.local.AppDatabase
import com.dt5gen.ecosystem.data.local.BinHistoryDao
import com.dt5gen.ecosystem.data.local.LocalDataSource
import com.dt5gen.ecosystem.data.repository.BinApiService
import com.dt5gen.ecosystem.data.repository.BinRepositoryImpl
import com.dt5gen.ecosystem.data.repository.LocalBinRepository
import com.dt5gen.ecosystem.domain.repository.BinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "bin_history_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBinHistoryDao(appDatabase: AppDatabase): BinHistoryDao {
        return appDatabase.binHistoryDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(binHistoryDao: BinHistoryDao): LocalBinRepository {
        return LocalDataSource(binHistoryDao) // LocalDataSource реализует LocalBinRepository
    }

    @Provides
    @Singleton
    fun provideBinApiService(retrofit: Retrofit): BinApiService {
        return retrofit.create(BinApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBinRepository(
        apiService: BinApiService,
        localBinRepository: LocalBinRepository
    ): BinRepository {
        return BinRepositoryImpl(apiService, localBinRepository)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://lookup.binlist.net/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}


