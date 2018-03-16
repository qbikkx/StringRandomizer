package com.qbikkx.base.util

import io.reactivex.annotations.CheckReturnValue
import io.reactivex.Observable
import io.reactivex.annotations.SchedulerSupport

/**
 * Created by qbikkx on 16.03.18.
 */
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
    checkNotNull(clazz) { "clazz is null" }
    return filter { !clazz.isInstance(it) }
}