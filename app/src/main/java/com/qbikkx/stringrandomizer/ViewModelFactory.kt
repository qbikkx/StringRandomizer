package com.qbikkx.stringrandomizer

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Created by qbikkx on 16.03.18.
 */
class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            is StringsViewModel::class -> StringsViewModel()
            else -> null
        }

}