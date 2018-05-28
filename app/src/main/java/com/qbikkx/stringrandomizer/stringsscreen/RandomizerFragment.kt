package com.qbikkx.stringrandomizer.stringsscreen

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import com.qbikkx.base.ui.BaseFragment
import com.qbikkx.base.util.observeNotNull
import com.qbikkx.stringrandomizer.R
import com.qbikkx.stringrandomizer.di.ActivityScoped
import com.qbikkx.stringrandomizer.stringsscreen.paging.StringsAdapter
import io.reactivex.disposables.CompositeDisposable
import kotterknife.bindView
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by qbikkx on 16.03.18.
 */
@ActivityScoped
class RandomizerFragment @Inject constructor() : BaseFragment() {

	val stringsList: RecyclerView by bindView(R.id.strings_list)
	val swipeRefresh by bindView<SwipeRefreshLayout>(R.id.swipe_refresh)
	val addBtn: FloatingActionButton by bindView(R.id.add_new_string_fab)
	val deleteBtn by bindView<FloatingActionButton>(R.id.delete_string_fab)
	val insertBtn by bindView<FloatingActionButton>(R.id.insert_new_string_fab)
	val slideBtn by bindView<FloatingActionButton>(R.id.slide_fab)
	val progressBar: ProgressBar by bindView(R.id.progress_bar)
	val noDataView: TextView by bindView(R.id.no_string_view)
	val sortOrderSwitch: Switch by bindView(R.id.sort_order_switch)

	private lateinit var stringsAdapter: StringsAdapter
	private val disposables = CompositeDisposable()

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var viewModel: StringsViewModel

	override fun getLayoutRes(): Int = R.layout.fragment_randomizer

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		stringsAdapter = StringsAdapter()
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(StringsViewModel::class.java)
		viewModel.stateLiveData.observeNotNull(this, this::render)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		stringsList.layoutManager = LinearLayoutManager(activity!!)
		stringsList.adapter = stringsAdapter

		swipeRefresh.setOnRefreshListener { viewModel.forceRefresh() }
		addBtn.setOnClickListener { viewModel.addString() }
		insertBtn.setOnClickListener { viewModel.forceInsert() }
		deleteBtn.setOnClickListener { viewModel.delete() }
		slideBtn.setOnClickListener { viewModel.slide() }
		sortOrderSwitch.setOnClickListener {
			viewModel.changeSort()
			sortOrderSwitch.text = resources.getText(if (sortOrderSwitch.isChecked) R.string.sort_by_value
			                                         else R.string.sort_by_hash)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		disposables.dispose()
	}


	fun render(state: StringsViewModel.ViewState) {
		Timber.e(state.toString())
		if (state.savingsCounter > 0) {
			progressBar.visibility = View.VISIBLE
		} else {
			progressBar.visibility = View.GONE
		}

		state.paging?.let {
			showList()
			stringsAdapter.submitList(state.paging)
		} ?: showEmptyList()

		state.scrollEvent?.content?.let { stringsList.scrollToPosition(it) }
	}

	private fun showEmptyList() {
		stringsList.visibility = View.GONE
		noDataView.visibility = View.VISIBLE
	}

	private fun showList() {
		stringsList.visibility = View.VISIBLE
		noDataView.visibility = View.GONE
	}
}