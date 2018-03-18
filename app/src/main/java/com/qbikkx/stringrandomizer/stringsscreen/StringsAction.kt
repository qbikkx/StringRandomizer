package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.mvi.BaseAction
import com.qbikkx.data.hashstring.HashString

/**
 * Created by Sviat on 17.03.2018.
 */
sealed class StringsAction : BaseAction {

    data class LoadStringsAction(val order: SortOrder) : StringsAction()

    data class StoreStringAction(val hashString: HashString) : StringsAction()
}