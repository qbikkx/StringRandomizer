package com.qbikkx.stringrandomizer

import com.qbikkx.base.mvi.BaseViewState
import com.qbikkx.data.hashstring.HashString

/**
 * Created by qbikkx on 16.03.18.
 */
data class StringsViewState(val isLoading: Boolean,
                            val isSaving: Boolean,
                            val isReordering: Boolean,
                            val strings: List<HashString>) : BaseViewState