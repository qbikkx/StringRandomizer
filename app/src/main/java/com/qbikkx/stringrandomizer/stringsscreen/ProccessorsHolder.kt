package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.data.hashstring.source.HashStringRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by Sviat on 18.03.2018.
 */
class ProccessorsHolder(stringsRepository: HashStringRepository, schedulers: RxSchedulers) {

    val loadHashStringsProccessor =
            ObservableTransformer<StringsAction.LoadStringsAction, StringsResult.LoadStringResult> { actions ->
                actions.flatMap { action ->
                    stringsRepository.getHashStrings()
                            .toObservable()
                            .map { strings ->
                                if (action.order == SortOrder.HASH)
                                    strings.sortedBy { it.hash }
                                else
                                    strings.sortedBy { it.string }
                            }
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
}