package com.qbikkx.data.hashstring.source

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.local.HashStringDao

/**
 * Created by Sviat on 17.03.2018.
 */
@Database(entities = [HashString::class], version = 1)
abstract class StringsRandomizerDatabase : RoomDatabase() {

    abstract fun hashStringDao(): HashStringDao
}