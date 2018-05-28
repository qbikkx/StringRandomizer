package com.qbikkx.base.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * Created by qbikkx on 16.03.18.
 */
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
	checkNotNull(clazz) { "clazz is null" }
	return filter { !clazz.isInstance(it) }
}

/**
 * Subscribes to this observable on IO thread, observes on Android's main thread
 * and handles all possible outcomes
 */
fun <T, R> Observable<T>.wrapWithResult(success: (T) -> R,
                                        error: (String) -> R,
                                        loading: () -> R
): Observable<R> {
	return this
			.subscribeOn(Schedulers.io())
			.map(success)
			.onErrorReturn { error(it.message ?: "") }
			.observeOn(AndroidSchedulers.mainThread())
			.startWith(loading())
}

fun <R> Completable.wrapWithResult(success: () -> R,
                                      error: (String) -> R,
                                      loading: () -> R
): Observable<R> {
	return this
			.subscribeOn(Schedulers.io())
			.andThen(Observable.defer{Observable.just(success())})
			.onErrorReturn { error(it.message ?: "") }
			.observeOn(AndroidSchedulers.mainThread())
			.startWith(loading())
}

fun <T1, T2> Observable<T1>.withLatest(other: Observable<T2>): Observable<Pair<T1, T2>> {
	return withLatestFrom(other, BiFunction { t1, t2 -> t1 to t2 })
}

fun <T1, T2> Observable<T1>.replaceWithLatest(other: Observable<T2>): Observable<T2> {
	return withLatestFrom(other, BiFunction { _, t2 -> t2 })
}

fun <T, R> Observable<T>.forkJoin(vararg func: Observable<T>.() -> Observable<R>): Observable<R> {
	return publish { shared ->
		Observable.merge(func.map { shared.it() })
	}
}