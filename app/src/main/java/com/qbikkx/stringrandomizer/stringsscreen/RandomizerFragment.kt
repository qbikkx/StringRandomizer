package com.qbikkx.stringrandomizer.stringsscreen

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import com.qbikkx.base.mvi.BaseView
import com.qbikkx.base.ui.BaseFragment
import com.qbikkx.stringrandomizer.R
import com.qbikkx.stringrandomizer.RandomizerViewModelFactory
import com.qbikkx.stringrandomizer.di.ActivityScoped
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by qbikkx on 16.03.18.
 */
@ActivityScoped
class RandomizerFragment @Inject constructor() : BaseFragment(), BaseView<StringsIntent, StringsViewState> {

    val stringsList: RecyclerView by bindView(R.id.strings_list)
    val addBtn: FloatingActionButton by bindView(R.id.add_new_string_fab)
    val progressBar: ProgressBar by bindView(R.id.progress_bar)
    val noDataView: TextView by bindView(R.id.no_string_view)
    val sortOrderSwitch: Switch by bindView(R.id.sort_order_switch)

    private lateinit var stringsAdapter: StringsAdapter
    private val disposables = CompositeDisposable()

    private val addStringIntentPublisher = PublishSubject.create<StringsIntent.AddStringIntent>()
    private val changeSortIntentPublisher = PublishSubject.create<StringsIntent.SortOrderChangedIntent>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: StringsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory)
                .get(StringsViewModel::class.java)
    }

    override fun getLayoutRes(): Int = R.layout.fragment_randomizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stringsAdapter = StringsAdapter(ArrayList(0))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stringsList.layoutManager = LinearLayoutManager(activity!!)
        stringsList.adapter = stringsAdapter
        disposables.add(viewModel.states().subscribe(this::render, Timber::e))
        viewModel.processIntents(intents())
        addBtn.setOnClickListener { addStringIntentPublisher.onNext(StringsIntent.AddStringIntent) }
        sortOrderSwitch.setOnClickListener {
            changeSortIntentPublisher.onNext(StringsIntent.SortOrderChangedIntent(
                    if (sortOrderSwitch.isChecked) SortOrder.VALUE
                    else SortOrder.HASH))
        }
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
            showList()
            stringsAdapter.setData(state.strings)
        }
    }

    private fun showEmptyList() {
        stringsList.visibility = View.GONE
        noDataView.visibility = View.VISIBLE
    }

    private fun showList() {
        stringsList.visibility = View.VISIBLE
        noDataView.visibility = View.GONE
    }

    private fun initialIntent(): Observable<StringsIntent.InitialIntent> =
            Observable.just(StringsIntent.InitialIntent)

    private fun addStringIntent(): Observable<StringsIntent.AddStringIntent> =
            addStringIntentPublisher

    private fun sortOrderChangedIntent(): Observable<StringsIntent.SortOrderChangedIntent> =
            changeSortIntentPublisher
}