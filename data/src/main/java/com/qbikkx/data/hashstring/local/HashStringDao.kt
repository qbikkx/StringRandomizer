package com.qbikkx.data.hashstring.local

import android.arch.persistence.room.*
import com.qbikkx.data.hashstring.HashString
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
@Dao
internal abstract class HashStringDao {

    @Query("SELECT * FROM HashStrings")
    abstract fun getAllHashStrings(): Single<List<HashString>>

    @Insert
    protected abstract fun insertHashString(hashString: HashString): Long

    @Insert
    abstract fun insertHashStrings(hashStrings: List<HashString>) : List<Long>

    @Update
    protected abstract fun updateHashString(hashString: HashString)

    @Delete
    abstract fun deleteAllHashStrings(): Completable

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