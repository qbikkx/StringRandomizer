package com.qbikkx.stringrandomizer.di

import javax.inject.Scope

/**
 * Created by Sviat on 17.03.2018.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class FragmentScoped