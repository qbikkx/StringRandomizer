package com.qbikkx.stringrandomizer

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.qbikkx.base.util.RxSchedulers
import com.qbikkx.data.hashstring.source.HashStringRepository
import com.qbikkx.stringrandomizer.stringsscreen.StringsViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class RandomizerViewModelFactory @Inject constructor(val stringsRepo: HashStringRepository,
                                                     val schedulers: RxSchedulers) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass == StringsViewModel::class.java) {
            return StringsViewModel(stringsRepo, schedulers) as T
        }
        throw IllegalArgumentException("unknown model class " + modelClass)
    }
}