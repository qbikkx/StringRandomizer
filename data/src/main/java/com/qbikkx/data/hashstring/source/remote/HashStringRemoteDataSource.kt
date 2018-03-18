package com.qbikkx.data.hashstring.remote

import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.source.HashStringDataSource
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * No remote source = no realisation :)
 */
@Singleton
class HashStringRemoteDataSource @Inject constructor(): HashStringDataSource {
    override fun getHashStrings(): Single<List<HashString>> = Single.just(emptyList())


    override fun saveHashString(hashString: HashString): Single<HashString> = Single.just(hashString)


    override fun deleteHashStrings(): Completable = Completable.complete()
}