package com.qbikkx.data.hashstring.source

import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.di.Local
import com.qbikkx.data.hashstring.di.Remote
import com.qbikkx.data.hashstring.local.HashStringLocalDataSource
import com.qbikkx.data.hashstring.remote.HashStringRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by qbikkx on 15.03.18.
 */
@Singleton
class HashStringRepository @Inject constructor(@Local val localDataSource: HashStringLocalDataSource,
                                               @Remote val remoteDataSource: HashStringRemoteDataSource) : HashStringDataSource {

    private var cacheIsDirty: Boolean = false

    private var cachedHashStrings: MutableMap<Long, HashString>? = null

    override fun getHashStrings(): Single<List<HashString>> {
        if (cachedHashStrings != null && !cacheIsDirty) {
            return Single.just(cachedHashStrings!!.values.toList())
        } else if (cachedHashStrings == null) {
            cachedHashStrings = LinkedHashMap()
        }

        val remoteHashStrings = getAndSaveRemoteHashStrings()

        return if (cacheIsDirty) {
            remoteHashStrings
        } else {
            val localHashStrings = getAndCacheLocalHashStrings()
            Single.concat(localHashStrings, remoteHashStrings)
                    .filter { !it.isEmpty() }
                    .firstOrError()
        }
    }

    override fun saveHashString(hashString: HashString) = Single.fromCallable {
        Thread.sleep(4000)
        remoteDataSource.saveHashString(hashString)
        val hashStr = localDataSource.saveHashString(hashString).blockingGet()

        if (cachedHashStrings == null) {
            cachedHashStrings = LinkedHashMap()
        }

        cachedHashStrings!![hashStr.id!!] = hashStr
        hashStr
    }

    override fun deleteHashStrings() = Completable.fromAction {
        remoteDataSource.deleteHashStrings()
        localDataSource.deleteHashStrings()

        if (cachedHashStrings != null) {
            cachedHashStrings!!.clear()
        }
    }

    private fun getAndCacheLocalHashStrings(): Single<List<HashString>> =
            localDataSource.getHashStrings().flatMap {
                Observable.fromIterable(it).doOnNext {
                    cachedHashStrings!![it.id!!] = it
                }.toList()
            }

    private fun getAndSaveRemoteHashStrings(): Single<List<HashString>> =
            remoteDataSource.getHashStrings().flatMap {
                Observable.fromIterable(it).doOnNext {
                    localDataSource.saveHashString(it)
                    cachedHashStrings!![it.id!!] = it
                }.toList()
            }.doOnSuccess { cacheIsDirty = false }
}