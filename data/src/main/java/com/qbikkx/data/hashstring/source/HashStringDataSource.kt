package com.qbikkx.data.hashstring.source

import android.arch.paging.PagedList
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.SortOrder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by qbikkx on 15.03.18.
 */
interface HashStringDataSource {

	fun getHashStrings(order: SortOrder): RepoPagingInteractor<HashString>

	fun saveHashString(hashString: HashString): Single<HashString>

	fun deleteHashStrings(): Completable

	fun deleteHashString(id: Long): Completable

	fun forceInsert(hashString: HashString): Single<HashString>
}