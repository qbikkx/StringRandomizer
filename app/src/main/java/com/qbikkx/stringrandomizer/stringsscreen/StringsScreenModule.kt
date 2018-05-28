package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.stringrandomizer.di.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Sviat on 17.03.2018.
 */
@Module
abstract class StringsScreenModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun stringsFragment(): RandomizerFragment
}