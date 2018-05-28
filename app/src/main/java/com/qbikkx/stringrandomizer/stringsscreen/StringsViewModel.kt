package com.qbikkx.stringrandomizer.stringsscreen

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.qbikkx.base.mvi.BaseAction
import com.qbikkx.base.mvi.BaseIntent
import com.qbikkx.base.mvi.BaseResult
import com.qbikkx.base.mvi.BaseViewModel
import com.qbikkx.base.ui.SingleLiveEvent
import com.qbikkx.base.util.*
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.SortOrder
import com.qbikkx.data.hashstring.source.HashStringRepository
import com.qbikkx.data.hashstring.source.RepoPagingInteractor
import io.reactivex.BackpressureStrategy
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Created by qbikkx on 16.03.18.
 */

class StringsViewModel @Inject constructor(private val repo: HashStringRepository) :
		ViewModel(), BaseViewModel {

	private val intentsRelay = PublishRelay.create<Intent>()
	private val stateStream = BehaviorRelay.create<ExtendedState>().apply { startWith(ExtendedState.default) }
	var ID_UPDATE = 0L

	private val intentFilter: ObservableTransformer<Intent, Intent>
		get() = ObservableTransformer { intents ->
			intents.forkJoin(
					{
						ofType(Intent.Initial::class.java).take(1)
								.cast(Intent::class.java)
					},
					{ notOfType(Intent.Initial::class.java) }
			)
		}

	private val intentToAction = ObservableTransformer<Intent, Action> {
		it.withLatest(stateStream)
				.map { (intent, state) ->
					when (intent) {
						Intent.Initial             -> Action.InitPagination(state.order)
						Intent.AddString           -> {
							val generated = ('a'..'z').randomString(6)
							if (ID_UPDATE != 0L) {
								HashString(id = ID_UPDATE, string = generated)
							} else {
								HashString(string = generated)
							}.let { Action.StoreString(it) }

						}
						Intent.InsertString        -> {
							val generated = ('a'..'z').randomString(6)
							if (ID_UPDATE != 0L) {
								HashString(id = ID_UPDATE, string = generated)
							} else {
								state.paging?.get(0)?.copy(string = generated)
								?: HashString(string = generated)
							}.let {
								Action.InsertString(it)
							}
						}
						Intent.DeleteString        -> {
							if (ID_UPDATE != 0L) {
								ID_UPDATE
							} else {
								state.paging?.pagedList.get(0)?.id ?: 0L
							}.let { Action.DeleteString(it) }
						}
						Intent.Slide               -> Action.Slide
						Intent.Refresh             -> Action.Refresh
						is Intent.SortOrderChanged ->
							Action.InitPagination(if (state.order == SortOrder.HASH) SortOrder.VALUE
							                      else SortOrder.HASH)
					}
				}
	}

	private val actionToResult = ObservableTransformer<Action, Result> {
		it.forkJoin(
				{
					ofType(Action.InitPagination::class.java)
							.map { action ->
								repo.getHashStrings(action.order)
							}
							.cast(Result::class.java)
				},
				{
					ofType(Action.StoreString::class.java)
							.flatMap {
								repo.saveHashString(it.hashString)
										.toObservable()
										.wrapWithResult({ Result.AddString.Success(it) },
										                { Result.AddString.Failure(it) },
										                { Result.AddString.InFlight })

							}
				},
				{ ofType(Action.Slide::class.java).map { Result.Scroll } },
				{
					ofType(Action.InsertString::class.java).flatMap {
						repo.forceInsert(it.hashString)
								.toObservable()
								.wrapWithResult({ Result.ForceInsert.Success(it) },
								                { Result.ForceInsert.Failure(it) },
								                { Result.ForceInsert.InFlight })
					}
				},
				{
					ofType(Action.DeleteString::class.java)
							.flatMap {
								repo.deleteHashString(it.stringId)
										.wrapWithResult({ Log.e("wew","notgood")
											                Result.Delete.Success },
										                {Log.e("wew", "err")
											                Result.Delete.Failure(it) },
										                { Result.Delete.InFlight })
							}
				}
		)
	}

	private val reducer = BiFunction<ExtendedState, Result, ExtendedState> { prev, result ->
		when (result) {
			is Result.InitPaging  -> {
				if (result.paging.size > 100) {
					ID_UPDATE = result.paging[100]?.id ?: 0L
				}
				prev.copy(paging = result.paging, order = result.order)
			}
			is Result.AddString   -> when (result) {
				is Result.AddString.Success -> prev.copy(savingsCounter = prev.savingsCounter.dec())
				is Result.AddString.Failure -> prev.copy(savingsCounter = prev.savingsCounter.dec())
				Result.AddString.InFlight   -> prev.copy(savingsCounter = prev.savingsCounter.inc())
			}
			is Result.Delete      -> when (result) {
				is Result.Delete.Success  -> prev
				is Result.Delete.Failure  -> prev
				is Result.Delete.InFlight -> prev
			}
			is Result.ForceInsert -> when (result) {
				is Result.ForceInsert.Success  -> prev
				is Result.ForceInsert.Failure  -> prev
				is Result.ForceInsert.InFlight -> prev
			}
			is Result.Scroll      -> prev.copy(scrollEvent = prev.paging
					?.let {
						SingleLiveEvent(200)
					})
		}
	}

	private val stateMapper = Function<ExtendedState, ViewState> {
		ViewState(savingsCounter = it.savingsCounter,
		          paging = it.paging,
		          error = it.loadError,
		          scrollEvent = it.scrollEvent)
	}

	private val disposable: Disposable

	val stateLiveData = LiveDataReactiveStreams.fromPublisher(
			stateStream
					.map(stateMapper)
					.distinctUntilChanged()
					.toFlowable(BackpressureStrategy.LATEST)
	)

	init {
		disposable = intentsRelay
				.compose(intentFilter)
				.compose(intentToAction)
				.compose(actionToResult)
				.scan(ExtendedState.default, reducer)
				.subscribe(stateStream)
		intentsRelay.accept(Intent.Initial)
	}

	override fun onCleared() {
		disposable.dispose()
		super.onCleared()
	}

	fun addString() = intentsRelay.accept(Intent.AddString)

	fun changeSort() = intentsRelay.accept(Intent.SortOrderChanged)

	fun forceInsert() = intentsRelay.accept(Intent.InsertString)

	fun delete() = intentsRelay.accept(Intent.DeleteString)
	fun forceRefresh() = intentsRelay.accept(Intent.Refresh)
	fun slide() = intentsRelay.accept(Intent.Slide)

	sealed class Intent : BaseIntent {
		object Refresh : Intent()
		object Initial : Intent()
		object Slide : Intent()
		object AddString : Intent()
		object InsertString : Intent()
		object DeleteString : Intent()
		object SortOrderChanged : Intent()
	}

	sealed class Action : BaseAction {
		data class InitPagination(val order: SortOrder) : Action()
		data class StoreString(val hashString: HashString) : Action()
		data class DeleteString(val stringId: Long) : Action()
		data class InsertString(val hashString: HashString) : Action()
		object Refresh : Action()
		object Slide : Action()
	}

	sealed class Result : BaseResult {

		data class InitPaging(val paging: RepoPagingInteractor<HashString>, val order: SortOrder) : Result()
		object Scroll : Result()
		sealed class AddString : Result() {
			data class Success(val string: HashString) : AddString()
			data class Failure(val error: String) : AddString()
			object InFlight : AddString()
		}

		sealed class ForceInsert : Result() {
			data class Success(val string: HashString) : ForceInsert()
			data class Failure(val error: String) : ForceInsert()
			object InFlight : ForceInsert()
		}

		sealed class Delete : Result() {
			object Success : Delete()
			data class Failure(val error: String) : Delete()
			object InFlight : Delete()
		}

	}

	private data class ExtendedState(val paging: RepoPagingInteractor<HashString>?,
	                                 val loadError: SingleLiveEvent<String>?,
	                                 val savingsCounter: Int,
	                                 val order: SortOrder,
	                                 val scrollEvent: SingleLiveEvent<Int>?) {

		companion object {

			val default = ExtendedState(paging = null,
			                            loadError = null,
			                            savingsCounter = 0,
			                            order = SortOrder.HASH,
			                            scrollEvent = null)
		}
	}

	data class ViewState(val savingsCounter: Int,
	                     val paging: PagedList<HashString>?,
	                     val error: SingleLiveEvent<String>?,
	                     val scrollEvent: SingleLiveEvent<Int>?)
}