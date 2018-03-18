package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.data.hashstring.source.HashStringRepository
import com.qbikkx.stringrandomizer.di.ActivityScoped
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

/**
 * Created by Sviat on 18.03.2018.
 */
@ActivityScoped
class StringsActionProccessorHolder @Inject constructor(val stringsRepository: HashStringRepository,
                                                        val schedulers: RxSchedulers) {


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
                            .andThen(stringsRepository.getHashStrings())
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
}