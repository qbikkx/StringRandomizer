package com.qbikkx.stringrandomizer

import com.qbikkx.base.mvi.BaseIntent

/**
 * Created by qbikkx on 16.03.18.
 */
sealed class StringsIntent : BaseIntent {

    object InitialIntent : StringsIntent()

    object AddStringIntent : StringsIntent()
}
