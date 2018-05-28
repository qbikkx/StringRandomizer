package com.qbikkx.data.hashstring.source

import android.arch.paging.PagedList
import com.jakewharton.rxrelay2.PublishRelay
import com.qbikkx.base.util.PagingRequestHelper
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.local.HashStringLocalDataSource
import com.qbikkx.data.hashstring.remote.HashStringRemoteDataSource
import com.qbikkx.data.hashstring.util.createStatusStream
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class HashBoundaryCallback(private val cache: HashStringLocalDataSource,
                           private val remote: HashStringRemoteDataSource)
	: PagedList.BoundaryCallback<HashString>() {

	private val helper = PagingRequestHelper(null)
	val networkStateStream = helper.createStatusStream()
	companion object {
		private const val NETWORK_PAGE_SIZE = 50
	}

	// keep the last requested page. When the request is successful, increment the page number.
	private var lastRequestedPage = 1

	private val _networkErrors = PublishRelay.create<String>()
	// LiveData of network errors.
	val networkErrors: Observable<String>
		get() = _networkErrors


	// avoid triggering multiple requests in the same time
	private var isRequestInProgress = false


	override fun onZeroItemsLoaded() {
		requestAndSaveData()
	}

	override fun onItemAtEndLoaded(itemAtEnd: HashString) {
		requestAndSaveData()
	}

	private fun requestAndSaveData() {
		if (isRequestInProgress) return

		isRequestInProgress = true

		remote.getHashStrings()
				.subscribeOn(Schedulers.single())
				.flatMapCompletable {
					Thread.sleep(3000)
					cache.saveHashStrings(it)
				}
				.doAfterTerminate { isRequestInProgress = false }
				.subscribe()
	}
}