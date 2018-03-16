package com.qbikkx.base.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by qbikkx on 16.03.18.
 */
data class RxSchedulers(
        val database: Scheduler = Schedulers.single(),
        val disk: Scheduler = Schedulers.io(),
        val network: Scheduler = Schedulers.io(),
        val main: Scheduler = AndroidSchedulers.mainThread(),
        val computation: Scheduler = Schedulers.computation())