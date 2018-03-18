package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.mvi.BaseViewState
import com.qbikkx.data.hashstring.HashString

/**
 * Created by qbikkx on 16.03.18.
 */
data class StringsViewState(val isLoading: Boolean,
                            val isSaving: Boolean,
                            val strings: List<HashString>,
                            val sortOrder: SortOrder) : BaseViewState {

    companion object {
        fun idle(): StringsViewState = StringsViewState(
                isLoading = false,
                isSaving = false,
                strings = emptyList(),
                sortOrder = SortOrder.HASH)
    }
}