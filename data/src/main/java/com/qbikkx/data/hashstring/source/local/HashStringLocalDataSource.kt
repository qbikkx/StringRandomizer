package com.qbikkx.data.hashstring.local

import android.arch.paging.DataSource
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.SortOrder
import com.qbikkx.data.hashstring.source.StringsRandomizerDatabase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by qbikkx on 15.03.18.
 */
@Singleton
class HashStringLocalDataSource @Inject constructor(val db: StringsRandomizerDatabase) {

	private val hashStringDao = db.hashStringDao()

	fun getHashStrings(): DataSource.Factory<Int, HashString> {
		return hashStringDao.getAllHashStrings()
	}

	fun saveHashString(hashString: HashString): Single<HashString> {
		return Single.fromCallable { hashStringDao.insertOrUpdateShow(hashString) }
	}

	fun forceInsert(hashString: HashString): Single<HashString> {
		return Single.fromCallable {
			val id = hashStringDao.insertHashString(hashString)
			hashString.copy(id = id)
		}
	}

	fun saveHashStrings(hashStrings: List<HashString>): Completable {
		return Completable.fromCallable { hashStringDao.insertHashStrings(hashStrings) }
	}

	fun deleteHashStrings(): Completable {
		return hashStringDao.deleteAllHashStrings()
	}

	fun deleteHashString(id: Long): Completable {
		return hashStringDao.deleteHashString(id)
	}
}