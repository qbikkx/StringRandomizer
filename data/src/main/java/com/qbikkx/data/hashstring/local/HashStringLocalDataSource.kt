package com.qbikkx.data.hashstring.local

import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.HashStringDataSource
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
internal class HashStringLocalDataSource(private val hashStringDao: HashStringDao) : HashStringDataSource {

    override fun getHashStrings(): Single<List<HashString>> = hashStringDao.getAllHashStrings()

    override fun saveHashString(hashString: HashString): Completable =
            Completable.fromAction({ hashStringDao.insertOrUpdateShow(hashString) })

    override fun deleteHashStrings(): Completable = hashStringDao.deleteAllHashStrings()
}