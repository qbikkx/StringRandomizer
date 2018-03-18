package com.qbikkx.stringrandomizer

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.qbikkx.stringrandomizer.stringsscreen.StringsActionProccessorHolder
import com.qbikkx.stringrandomizer.stringsscreen.StringsViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(val proccessorHolder: StringsActionProccessorHolder)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == StringsViewModel::class.java) {
            return StringsViewModel(proccessorHolder) as T
        }
        throw IllegalArgumentException("unknown model class " + modelClass)
    }
}