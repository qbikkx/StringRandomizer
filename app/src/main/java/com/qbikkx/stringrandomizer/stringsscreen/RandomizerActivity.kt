package com.qbikkx.stringrandomizer.stringsscreen

import android.os.Bundle
import com.qbikkx.base.ui.BaseActivity
import com.qbikkx.base.ui.addFragmentToActivity
import com.qbikkx.stringrandomizer.R
import dagger.Lazy
import javax.inject.Inject

class RandomizerActivity : BaseActivity() {

    @Inject
    lateinit var randromizerFragmentProvider: Lazy<RandomizerFragment>
    override fun getLayoutRes(): Int = R.layout.activity_randomizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            addFragmentToActivity(supportFragmentManager, randromizerFragmentProvider.get(),
                    R.id.fragment_container)
        }
    }
}
