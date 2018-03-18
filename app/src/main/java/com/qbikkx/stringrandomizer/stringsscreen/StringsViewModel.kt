package com.qbikkx.stringrandomizer.stringsscreen

import android.arch.lifecycle.ViewModel
import com.qbikkx.base.mvi.BaseViewModel
import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.base.util.notOfType
import com.qbikkx.base.util.randomString
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.data.hashstring.source.HashStringRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by qbikkx on 16.03.18.
 */
class StringsViewModel (val stringsRepository: HashStringRepository,
                                           val schedulers: RxSchedulers):
        ViewModel(), BaseViewModel<StringsIntent, StringsViewState> {

    val loadHashStringsProccessor =
            ObservableTransformer<StringsAction.LoadStringsAction, StringsResult.LoadStringResult> { actions ->
                actions.flatMap {
                    stringsRepository.getHashStrings()
                            .toObservable()
                            .map { strings -> StringsResult.LoadStringResult.Success(strings) }
                            .cast(StringsResult.LoadStringResult::class.java)
                            .onErrorReturn(StringsResult.LoadStringResult::Failure)
                            .subscribeOn(schedulers.network)
                            .observeOn(schedulers.main)
                            .startWith(StringsResult.LoadStringResult.InFlight)
                }
            }

    val saveHashStringProccessor =
            ObservableTransformer<StringsAction.StoreStringAction, StringsResult.AddStringResult> { actions ->
                actions.flatMap { action ->
                    stringsRepository.saveHashString(action.hashString)
                            .flatMap { stringsRepository.getHashStrings() }
                            .toObservable()
                            .map { strings -> StringsResult.AddStringResult.Success(strings) }
                            .cast(StringsResult.AddStringResult::class.java)
                            .onErrorReturn(StringsResult.AddStringResult::Failure)
                            .subscribeOn(schedulers.network)
                            .observeOn(schedulers.main)
                            .startWith(StringsResult.AddStringResult.InFlight)
                }
            }

    var actionProccessor =
            ObservableTransformer<StringsAction, StringsResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(StringsAction.LoadStringsAction::class.java)
                                    .compose(loadHashStringsProccessor),
                            shared.ofType(StringsAction.StoreStringAction::class.java)
                                    .compose(saveHashStringProccessor))
                            .mergeWith(
                                    shared.filter { v ->
                                        v !is StringsAction.LoadStringsAction &&
                                                v !is StringsAction.StoreStringAction
                                    }.flatMap { w ->
                                                Observable.error<StringsResult>(IllegalArgumentException("Unknown action type: $w"))
                                            }
                            )
                }
            }

    private val intentsSubject: PublishSubject<StringsIntent> = PublishSubject.create()
    private val statesObservable: Observable<StringsViewState> = compose()

    private val intentFilter: ObservableTransformer<StringsIntent, StringsIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                        shared.ofType(StringsIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(StringsIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<StringsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<StringsViewState> = statesObservable

    private fun compose(): Observable<StringsViewState> {
        return intentsSubject
                .compose(intentFilter)
                .map ( this::actionFromIntent )
                .compose(actionProccessor)
                .scan(StringsViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)

    }

    private fun actionFromIntent(intent: StringsIntent): StringsAction =
            when (intent) {
                StringsIntent.InitialIntent -> StringsAction.LoadStringsAction
                StringsIntent.AddStringIntent -> {
                    val generated = ('a'..'z').randomString(6)
                    StringsAction.StoreStringAction(HashString(string = generated))
                }
                is StringsIntent.SortOrderChangedIntent -> {
                    StringsAction.LoadStringsAction
                }
            }

    companion object {

        private val reducer = BiFunction { previousState: StringsViewState, result: StringsResult ->
            when (result) {
                is StringsResult.LoadStringResult -> when(result) {
                    is StringsResult.LoadStringResult.Success -> {
                        val strings = result.strings
                        previousState.copy(isLoading = false, strings = strings)
                    }
                    is StringsResult.LoadStringResult.Failure ->
                        previousState.copy(isLoading = false)
                    is StringsResult.LoadStringResult.InFlight ->
                            previousState.copy(isLoading = true)
                }
                is StringsResult.AddStringResult -> when (result) {
                    is StringsResult.AddStringResult.Success -> {
                        previousState.copy(isSaving = false, strings = result.strings)
                    }
                    is StringsResult.AddStringResult.Failure -> {
                        previousState.copy(isSaving = false)
                    }
                    is StringsResult.AddStringResult.InFlight -> {
                        previousState.copy(isSaving = true)
                    }
                }
            }
        }
    }
}