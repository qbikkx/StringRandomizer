package com.qbikkx.stringrandomizer.stringsscreen

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import com.qbikkx.base.mvi.BaseView
import com.qbikkx.base.ui.BaseFragment
import com.qbikkx.stringrandomizer.R
import com.qbikkx.stringrandomizer.ViewModelFactory
import com.qbikkx.stringrandomizer.di.ActivityScoped
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView
import javax.inject.Inject

/**
 * Created by qbikkx on 16.03.18.
 */
@ActivityScoped
class RandomizerFragment @Inject constructor(): BaseFragment(), BaseView<StringsIntent, StringsViewState> {

    val stringsList: RecyclerView by bindView(R.id.strings_list)
    val addBtn: FloatingActionButton by bindView(R.id.add_new_string_fab)
    val progressBar: ProgressBar by bindView(R.id.progress_bar)
    val noDataView: TextView by bindView(R.id.no_string_view)
    val sortOrderSwitch: Switch by bindView(R.id.sort_order_switch)

    private lateinit var stringsAdapter: StringsAdapter
    private val disposables = CompositeDisposable()

    private val addStringIntentPublisher = PublishSubject.create<StringsIntent.AddStringIntent>()

    @Inject
    lateinit var viewModeFactory: ViewModelFactory

    private val viewModel: StringsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
                .of(this, viewModeFactory)
                .get(StringsViewModel::class.java)
    }

    override fun getLayoutRes(): Int = R.layout.fragment_randomizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stringsAdapter = StringsAdapter(ArrayList(0))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())

    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun intents(): Observable<StringsIntent> = Observable.merge(
            initialIntent(),
            addStringIntent(),
            sortOrderChangedIntent())



    override fun render(state: StringsViewState) {
        if (state.isLoading || state.isReordering || state.isSaving) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }

        if (state.strings.isEmpty()) {
            showEmptyList()
        } else {
            stringsAdapter.setData(state.strings)
        }
    }

    private fun showEmptyList() {
        stringsList.visibility = View.GONE
        noDataView.visibility = View.VISIBLE
    }

    private fun initialIntent(): Observable<StringsIntent.InitialIntent> =
            Observable.just(StringsIntent.InitialIntent)

    private fun addStringIntent(): Observable<StringsIntent.AddStringIntent> =
            Observable.just(StringsIntent.AddStringIntent)

    private fun sortOrderChangedIntent(): Observable<StringsIntent.SortOrderChangedIntent> =
            Observable.just(StringsIntent.SortOrderChangedIntent(
                    if (sortOrderSwitch.isChecked) SortOrder.VALUE else SortOrder.HASH))
}