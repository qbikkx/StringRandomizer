package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.mvi.BaseIntent

/**
 * Created by qbikkx on 16.03.18.
 */
sealed class StringsIntent : BaseIntent {

    object InitialIntent : StringsIntent()

    object AddStringIntent : StringsIntent()

    data class SortOrderChangedIntent(val order: SortOrder) : StringsIntent()
}
