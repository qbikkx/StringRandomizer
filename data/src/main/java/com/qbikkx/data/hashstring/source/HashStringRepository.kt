package com.qbikkx.data.hashstring.source

import android.arch.paging.DataSource
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.qbikkx.base.util.sortedByWithDelay
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.SortOrder
import com.qbikkx.data.hashstring.di.Local
import com.qbikkx.data.hashstring.di.Remote
import com.qbikkx.data.hashstring.local.HashStringLocalDataSource
import com.qbikkx.data.hashstring.remote.HashStringRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by qbikkx on 15.03.18.
 */
@Singleton
class HashStringRepository @Inject constructor(@Local val localDataSource: HashStringLocalDataSource,
                                               @Remote val remoteDataSource: HashStringRemoteDataSource)
	: HashStringDataSource {

	override fun deleteHashString(id: Long): Completable {
		return remoteDataSource.deleteHashString(id)
				.andThen(localDataSource.deleteHashString(id))
	}

	override fun forceInsert(hashString: HashString): Single<HashString> {
		return remoteDataSource.saveHashString(hashString)
				.flatMap { localDataSource.forceInsert(hashString) }
	}

	override fun getHashStrings(order: SortOrder): RepoPagingInteractor<HashString> {
		val factory: DataSource.Factory<Int, HashString> = localDataSource.getHashStrings()
		val config = PagedList.Config.Builder()
				.setPageSize(40)
				.setInitialLoadSizeHint(100) //def: pagesize*3
				.setPrefetchDistance(15)//def: pagesize
				.setEnablePlaceholders(true)//def:false
				.build()
		val boundaryCallback = HashBoundaryCallback(cache = localDataSource,
		                                            remote = remoteDataSource)

		val strings = RxPagedListBuilder(factory, config)
				.setBoundaryCallback(boundaryCallback)
				.buildObservable()

		val refreshTrigger = PublishRelay.create<Any>()
		val refreshState = refreshTrigger.switchMap { refresh() }

		return RepoPagingInteractor(
				pagedList = strings,
				networkState = boundaryCallback.networkStateStream,
				retry = {},
				refresh = { refreshTrigger.accept(Any()) },
				refreshState = refreshState
		)
	}

	override fun saveHashString(hashString: HashString): Single<HashString> {
		return remoteDataSource.saveHashString(hashString)
				.doOnSuccess {
					Thread.sleep(4000L)
					localDataSource.saveHashString(it)
				}
	}

	override fun deleteHashStrings() = Completable.fromCallable {
		remoteDataSource.deleteHashStrings()
				.andThen(localDataSource.deleteHashStrings())
	}

	private fun refresh(): Observable<NetworkState> {
		val networkStateStream = BehaviorRelay.create<NetworkState>()
		remoteDataSource.getHashStrings()
				.doOnSubscribe { networkStateStream.accept(NetworkState.LOADING) }
				.subscribe({
					           localDataSource.db.runInTransaction {
						           localDataSource.deleteHashStrings()
						           localDataSource.saveHashStrings(it)
					           }
					           networkStateStream.accept(NetworkState.LOADED)
				           }, {
					           networkStateStream.accept(NetworkState.error(it.message))
				           })
		return networkStateStream
	}

	private fun sortStrings(strings: List<HashString>, order: SortOrder): List<HashString> =
			when (order) {
				SortOrder.HASH  -> strings.sortedByWithDelay { it.hash }
				SortOrder.VALUE -> strings.sortedByWithDelay { it.string }
			}
}