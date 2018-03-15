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
    abstract fun getAllHashStrings(): Observable<List<HashString>>

    @Insert
    abstract fun saveHashString(hashString: HashString): Single<Long>

    @Insert
    abstract fun saveHashStrings(hashStrings: List<HashString>) : Single<Long>

    @Update
    abstract fun updateHashString(hashString: HashString): Completable

    @Delete
    abstract fun deleteAllHashStrings(): Completable
}