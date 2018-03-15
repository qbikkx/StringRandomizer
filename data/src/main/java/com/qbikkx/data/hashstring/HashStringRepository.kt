package com.qbikkx.data.hashstring

import com.qbikkx.data.hashstring.local.HashStringLocalDataSource
import com.qbikkx.data.hashstring.remote.HashStringRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
class HashStringRepository internal constructor(val localDataSource: HashStringLocalDataSource,
                                                val remoteDataSource: HashStringRemoteDataSource) : HashStringDataSource {

    override fun getHashStrings(): Single<List<HashString>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveHashString(hashString: HashString): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveHashStrings(hashStrings: List<HashString>): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteHashStrings(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}