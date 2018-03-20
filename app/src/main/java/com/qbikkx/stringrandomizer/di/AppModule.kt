package com.qbikkx.stringrandomizer.di

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.stringrandomizer.RandomizerViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Sviat on 18.03.2018.
 */
@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: RandomizerViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun rxScheduler() = RxSchedulers()
    }
}