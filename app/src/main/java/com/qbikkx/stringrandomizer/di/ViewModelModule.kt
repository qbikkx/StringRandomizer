package com.qbikkx.stringrandomizer.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.qbikkx.stringrandomizer.RandomizerViewModelFactory
import com.qbikkx.stringrandomizer.stringsscreen.StringsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Sviat on 20.03.2018.
 */
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: RandomizerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StringsViewModel::class)
    abstract fun bindStringsViewModel(stringsViewModel: StringsViewModel): ViewModel
}