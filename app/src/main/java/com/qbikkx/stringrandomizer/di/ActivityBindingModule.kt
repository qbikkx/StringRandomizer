package com.qbikkx.stringrandomizer.di

import com.qbikkx.stringrandomizer.stringsscreen.RandomizerActivity
import com.qbikkx.stringrandomizer.stringsscreen.StringsScreenModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Sviat on 17.03.2018.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [StringsScreenModule::class])
    abstract fun randomizerActivity(): RandomizerActivity
}