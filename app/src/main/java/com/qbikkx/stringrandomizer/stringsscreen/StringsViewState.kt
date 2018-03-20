package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.mvi.BaseViewState
import com.qbikkx.data.hashstring.HashString

/**
 * Created by qbikkx on 16.03.18.
 */
data class StringsViewState(val loadingsCounter: Int,
                            val savingsCounter: Int,
                            val strings: List<HashString>,
                            val sortOrder: SortOrder) : BaseViewState {

    companion object {
        fun idle(): StringsViewState = StringsViewState(
                loadingsCounter = 0,
                savingsCounter = 0,
                strings = emptyList(),
                sortOrder = SortOrder.HASH)
    }
}