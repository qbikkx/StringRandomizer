package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.mvi.BaseResult
import com.qbikkx.data.hashstring.HashString

/**
 * Created by Sviat on 17.03.2018.
 */
sealed class StringsResult : BaseResult {

    sealed class LoadStringResult : StringsResult() {
        data class Success(val strings: List<HashString>, val order: SortOrder) : LoadStringResult()

        data class Failure(val error: Throwable) : LoadStringResult()

        object InFlight : LoadStringResult()
    }

    sealed class AddStringResult : StringsResult() {
        data class Success(val strings: List<HashString>) : AddStringResult()

        data class Failure(val error: Throwable) : AddStringResult()

        object InFlight : AddStringResult()
    }
}