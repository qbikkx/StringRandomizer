package com.qbikkx.data.hashstring.source

import com.qbikkx.data.hashstring.HashString
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
interface HashStringDataSource {

    fun getHashStrings() : Single<List<HashString>>

    fun saveHashString(hashString: HashString) : Completable

    fun deleteHashStrings() : Completable
}