package com.qbikkx.base.ui

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

@SuppressLint("CommitTransaction")
fun addFragmentToActivity(fragmentManager: FragmentManager,
                          fragment: Fragment,
                          frameId: Int) {
    fragmentManager.beginTransaction().run {
        add(frameId, fragment)
        commit()
    }
}