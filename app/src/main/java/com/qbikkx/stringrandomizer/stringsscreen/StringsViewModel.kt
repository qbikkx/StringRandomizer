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

/**
 * Created by qbikkx on 16.03.18.
 */

class StringsViewModel(stringsRepository: HashStringRepository, val schedulers: RxSchedulers) :
        ViewModel(), BaseViewModel<StringsIntent, StringsViewState> {

    private val proccessorsHolder = ProccessorsHolder(stringsRepository, schedulers)

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
                .map(this::actionFromIntent)
                .compose(proccessorsHolder.actionProccessor)
                .scan(StringsViewState.idle(), reducer)
                //предотвращаем повторный render предыдущего состояния
                .distinctUntilChanged()
                //восстанавливаем предыдущее событие при повороте экрана
                .replay(1)
                //держим цепочку живой даже когда вьюхи нет, чтобы совпадать с лайфсайклом ViewModel
                .autoConnect(0)

    }

    private fun actionFromIntent(intent: StringsIntent): StringsAction =
            when (intent) {
                StringsIntent.InitialIntent -> StringsAction.LoadStringsAction(SortOrder.HASH)
                StringsIntent.AddStringIntent -> {
                    val generated = ('a'..'z').randomString(6)
                    StringsAction.StoreStringAction(HashString(string = generated))
                }
                is StringsIntent.SortOrderChangedIntent -> {
                    StringsAction.LoadStringsAction(intent.order)
                }
            }

    companion object {

        private val reducer = BiFunction { previousState: StringsViewState, result: StringsResult ->
            when (result) {
                is StringsResult.LoadStringResult -> when (result) {
                    is StringsResult.LoadStringResult.Success -> {
                        //если сортировка была запущена раньше чем добавление, но выполнилась позже,
                        //то закидываем ранее добавленый элемент в конец.
                        val diff: List<HashString> = previousState.strings.minus(result.strings)
                        val viewStateStrings: List<HashString>
                        viewStateStrings = if (diff.isNotEmpty()) {
                            result.strings.plus(diff)
                        } else {
                            result.strings
                        }
                        previousState.copy(isLoading = false,
                                strings = viewStateStrings,
                                sortOrder = result.order)
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