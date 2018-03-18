package com.qbikkx.data.hashstring.local

import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.source.HashStringDataSource
import com.qbikkx.data.hashstring.source.StringsRandomizerDatabase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by qbikkx on 15.03.18.
 */
@Singleton
class HashStringLocalDataSource @Inject constructor(db: StringsRandomizerDatabase) : HashStringDataSource {

    private val hashStringDao = db.hashStringDao()

    override fun getHashStrings(): Single<List<HashString>> = hashStringDao.getAllHashStrings()

    override fun saveHashString(hashString: HashString): Completable =
            Completable.fromAction({ hashStringDao.insertOrUpdateShow(hashString) })

    override fun deleteHashStrings(): Completable {
        hashStringDao.deleteAllHashStrings()
        return Completable.complete()
    }
}