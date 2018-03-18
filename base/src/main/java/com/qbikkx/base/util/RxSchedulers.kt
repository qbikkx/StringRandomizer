package com.qbikkx.base.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by qbikkx on 16.03.18.
 */
data class RxSchedulers(
        val disk: Scheduler = Schedulers.io(),
        val repository: Scheduler = Schedulers.io(),
        val main: Scheduler = AndroidSchedulers.mainThread(),
        val computation: Scheduler = Schedulers.computation())