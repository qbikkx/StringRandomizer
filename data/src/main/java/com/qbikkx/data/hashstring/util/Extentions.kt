package com.qbikkx.data.hashstring.util

import com.jakewharton.rxrelay2.PublishRelay
import com.qbikkx.base.util.PagingRequestHelper
import com.qbikkx.data.hashstring.source.NetworkState
import io.reactivex.Observable

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
	return PagingRequestHelper.RequestType.values().mapNotNull {
		report.getErrorFor(it)?.message
	}.first()
}

fun PagingRequestHelper.createStatusStream(): Observable<NetworkState> {
	val networkStateStream = PublishRelay.create<NetworkState>()
	addListener { report ->
		when {
			report.hasRunning() -> networkStateStream.accept(NetworkState.LOADING)
			report.hasError() -> networkStateStream.accept(
					NetworkState.error(getErrorMessage(report)))
			else -> networkStateStream.accept(NetworkState.LOADED)
		}
	}
	return networkStateStream
}