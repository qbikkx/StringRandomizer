package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.data.hashstring.source.HashStringRepository
import com.qbikkx.stringrandomizer.ViewModelFactory
import com.qbikkx.stringrandomizer.di.ActivityScoped
import com.qbikkx.stringrandomizer.di.FragmentScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector


/**
 * Created by Sviat on 17.03.2018.
 */
@Module
abstract class StringsScreenModule {


    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun stringsFragment(): RandomizerFragment

    @Module
    companion object {

        @JvmStatic
        @Provides
        @ActivityScoped
        fun provideProccessorHolder(stringsRepository: HashStringRepository, schedulers: RxSchedulers)
                : StringsActionProccessorHolder {
            return StringsActionProccessorHolder(stringsRepository, schedulers)
        }

        @JvmStatic
        @ActivityScoped
        @Provides
        fun provideViewModeFactory(proccessorHolder: StringsActionProccessorHolder): ViewModelFactory {
            return ViewModelFactory(proccessorHolder)
        }

    }
}