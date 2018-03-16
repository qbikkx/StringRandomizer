package com.qbikkx.stringrandomizer

import android.arch.lifecycle.ViewModel
import com.qbikkx.base.mvi.BaseViewModel
import com.qbikkx.base.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

/**
 * Created by qbikkx on 16.03.18.
 */
class StringsViewModel : ViewModel(), BaseViewModel<StringsIntent, StringsViewState> {

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun states(): Observable<StringsViewState> = statesObservable

    private fun compose(): Observable<StringsViewState> {
        return intentsSubject
                .compose(intentFilter)

    }
}