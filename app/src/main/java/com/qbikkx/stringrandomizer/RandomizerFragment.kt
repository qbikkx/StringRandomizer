package com.qbikkx.stringrandomizer

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qbikkx.base.mvi.BaseView
import com.qbikkx.base.ui.BaseFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotterknife.bindView

/**
 * Created by qbikkx on 16.03.18.
 */
class RandomizerFragment: BaseFragment(), BaseView<StringsIntent, StringsViewState> {

    val stringsList: RecyclerView by bindView(R.id.strings_list)
    val addBtn: FloatingActionButton by bindView(R.id.add_new_string_fab)

    private lateinit var stringsAdapter: StringsAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun getLayoutRes(): Int = R.layout.fragment_randomizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stringsAdapter = StringsAdapter(ArrayList(0))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun intents(): Observable<StringsIntent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(state: StringsViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}