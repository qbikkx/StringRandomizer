package com.qbikkx.base.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by qbikkx on 16.03.18.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayoutRes(), container, false)

    @LayoutRes
    abstract fun getLayoutRes() : Int
}