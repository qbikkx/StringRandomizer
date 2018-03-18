package com.qbikkx.data.hashstring.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.qbikkx.data.hashstring.HashString
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
@Dao
abstract class HashStringDao {

    @Query("SELECT * FROM HashStrings")
    abstract fun getAllHashStrings(): Single<List<HashString>>

    @Insert
    protected abstract fun insertHashString(hashString: HashString): Long

    @Update
    protected abstract fun updateHashString(hashString: HashString)

    @Query("DELETE FROM HashStrings")
    abstract fun deleteAllHashStrings()

    fun insertOrUpdateShow(hashString: HashString): HashString = when {
        hashString.id == null -> {
            hashString.copy(id = insertHashString(hashString))
        }
        else -> {
            updateHashString(hashString)
            hashString
        }
    }
}