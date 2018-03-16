package com.qbikkx.stringrandomizer

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import com.qbikkx.base.ui.BaseActivity
import com.qbikkx.base.ui.addFragmentToActivity
import kotterknife.bindView

class RandomizerActivity : BaseActivity() {

    override fun getLayoutRes(): Int = R.layout.activity_randomizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            addFragmentToActivity(supportFragmentManager, RandomizerFragment(), R.id.fragment_container)
        }
    }
}
