package com.qbikkx.stringrandomizer

/**
 * Created by qbikkx on 16.03.18.
 */
interface Factory<T> {

    fun create(clazz: Class<T>): T
}