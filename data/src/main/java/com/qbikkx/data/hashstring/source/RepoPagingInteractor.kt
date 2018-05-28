package com.qbikkx.data.hashstring.source

import android.arch.paging.PagedList
import io.reactivex.Observable

data class RepoPagingInteractor<T>(
		// the LiveData of paged lists for the UI to observe
		val pagedList: Observable<PagedList<T>>,
		// represents the network request status to show to the user
		val networkState: Observable<NetworkState>,
		// represents the refresh status to show to the user. Separate from networkState, this
		// value is importantly only when refresh is requested.
		val refreshState: Observable<NetworkState>,
		// refreshes the whole data and fetches it from scratch.
		val refresh: () -> Unit,
		// retries any failed requests.
		val retry: () -> Unit
)