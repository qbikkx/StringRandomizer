package com.qbikkx.base.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

/**
 * Created by qbikkx on 16.03.18.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }

    @LayoutRes
    abstract fun getLayoutRes() : Int
}