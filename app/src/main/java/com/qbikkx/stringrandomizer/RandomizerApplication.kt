package com.qbikkx.stringrandomizer

import com.qbikkx.stringrandomizer.di.AppComponent
import com.qbikkx.stringrandomizer.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * Created by Sviat on 17.03.2018.
 */
open class RandomizerApplication : DaggerApplication() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        return appComponent
    }
}