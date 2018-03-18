package com.qbikkx.stringrandomizer.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.stringrandomizer.RandomizerViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.multibindings.Multibinds
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

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