package com.qbikkx.base.mvi

import io.reactivex.Observable

/**
 * Created by qbikkx on 16.03.18.
 */
interface BaseViewModel<I: BaseIntent, S: BaseViewState> {

    fun processIntents(intents: Observable<I>)

    fun states(): Observable<S>
}