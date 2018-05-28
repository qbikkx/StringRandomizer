package com.qbikkx.data.hashstring.source

import android.arch.paging.PositionalDataSource
import com.qbikkx.data.hashstring.HashString

class RuntimeCacheDataSource(private val cached: List<HashString>) : PositionalDataSource<HashString>() {

	override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<HashString>) {
		val totalCount = cached.size
		val position = computeInitialLoadPosition(params, totalCount)
		val loadSize = computeInitialLoadSize(params, position, totalCount)
		callback.onResult(loadRangeInternal(position, loadSize), position, totalCount)
	}

	override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<HashString>) {
		callback.onResult(loadRangeInternal(params.startPosition, params.loadSize))
	}

	private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<HashString> {
		return cached.subList(startPosition, startPosition + loadCount)
	}
}