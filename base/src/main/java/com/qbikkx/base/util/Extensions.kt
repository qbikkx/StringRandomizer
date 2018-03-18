package com.qbikkx.base.util

import java.util.*

/**
 * Created by Sviat on 18.03.2018.
 */
fun ClosedRange<Char>.randomString(lenght: Int) =
        (1..lenght)
                .map { (Random().nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
                .joinToString("")

/**
 * Returns a list of all elements sorted according to natural sort order of the value returned by specified [selector] function.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.sortedByWithDelay(crossinline selector: (T) -> R?): List<T> {
    Thread.sleep(2000)
    return sortedWith(compareBy(selector))
}