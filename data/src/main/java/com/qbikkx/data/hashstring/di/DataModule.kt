package com.qbikkx.data.hashstring.di

import android.arch.persistence.room.Room
import android.content.Context
import com.qbikkx.data.hashstring.local.HashStringLocalDataSource
import com.qbikkx.data.hashstring.remote.HashStringRemoteDataSource
import com.qbikkx.data.hashstring.source.StringsRandomizerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Sviat on 17.03.2018.
 */
@Module
class DataModule {

    @Singleton
    @Provides
    @Local
    fun provideHashStringLocalDataSource(db: StringsRandomizerDatabase) = HashStringLocalDataSource(db)

    @Singleton
    @Provides
    @Remote
    fun provideHashStringRemoteDataSource() = HashStringRemoteDataSource()


    @Singleton
    @Provides
    fun provideDatabase(context: Context): StringsRandomizerDatabase =
            Room.databaseBuilder(context, StringsRandomizerDatabase::class.java, "hashstrings.db")
                    .fallbackToDestructiveMigration()
                    .build()

}