package com.qbikkx.stringrandomizer.di

import android.app.Application
import com.qbikkx.data.hashstring.di.DataModule
import com.qbikkx.data.hashstring.source.HashStringRepository
import com.qbikkx.stringrandomizer.RandomizerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * Created by Sviat on 17.03.2018.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    DataModule::class,
    AppModule::class])
interface AppComponent : AndroidInjector<RandomizerApplication> {

    fun getHashStringsRepository() : HashStringRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}