package com.qbikkx.stringrandomizer.stringsscreen

import com.qbikkx.base.mvi.BaseAction
import com.qbikkx.data.hashstring.HashString

/**
 * Created by Sviat on 17.03.2018.
 */
sealed class StringsAction : BaseAction {

    object LoadStringsAction : StringsAction()

    data class StoreStringAction(val hashString: HashString) : StringsAction()
}