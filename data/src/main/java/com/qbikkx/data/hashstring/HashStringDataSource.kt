package com.qbikkx.data.hashstring

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
interface HashStringDataSource {

    fun getHashStrings() : Single<HashString>

    fun saveHashString(hashString: HashString) : Completable

    fun saveHashStrings(hashStrings: List<HashString>) : Completable

    fun deleteHashStrings() : Completable
}