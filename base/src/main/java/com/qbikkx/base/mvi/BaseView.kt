package com.qbikkx.base.mvi

import io.reactivex.Observable

/**
 * Created by qbikkx on 16.03.18.
 */
interface BaseView<I : BaseIntent, in S : BaseViewState> {

    /**
     * Unique [Observable] used by the [MviViewModel]
     * to listen to the [MviView].
     * All the [MviView]'s [MviIntent]s must go through this [Observable].
     */
    fun intents(): Observable<I>

    /**
     * Entry point for the [MviView] to render itself based on a [MviViewState].
     */
    fun render(state: S)
}