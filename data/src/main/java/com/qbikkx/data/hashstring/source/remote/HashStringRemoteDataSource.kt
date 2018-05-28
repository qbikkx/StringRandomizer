package com.qbikkx.data.hashstring.remote

import com.qbikkx.base.util.randomString
import com.qbikkx.data.hashstring.HashString
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * No remote source = no realisation :)
 */
@Singleton
class HashStringRemoteDataSource @Inject constructor() {

	fun getHashStrings(): Observable<List<HashString>> {
		return Observable.fromCallable {
			ArrayList<HashString>().apply {
				(1..50).forEach {
					var genereted = ('a'..'z').randomString(6)
					if (it == 50) genereted += "5000000000000000000"
					this.add(HashString(string = genereted))
				}
			}
		}
	}

	fun saveHashString(hashString: HashString): Single<HashString> = Single.just(hashString)

	fun deleteHashStrings(): Completable = Completable.complete()

	fun deleteHashString(id: Long) : Completable = Completable.complete()
}