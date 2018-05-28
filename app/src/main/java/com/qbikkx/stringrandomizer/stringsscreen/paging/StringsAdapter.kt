package com.qbikkx.stringrandomizer.stringsscreen.paging

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.stringrandomizer.R
import kotterknife.bindView

class StringsAdapter
	: PagedListAdapter<HashString, StringsAdapter.ViewHolder>(DIFF_CALLBACK) {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_string, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		val content by bindView<LinearLayout>(R.id.content)
		val progressBar by bindView<ProgressBar>(R.id.progress_bar)
		val value: TextView by bindView(R.id.item_string_value)
		val hash: TextView by bindView(R.id.item_string_hash)

		fun bind(hashString: HashString?) {
			hashString?.let {
				hideProgressBar()
				value.text = hashString.string
				hash.text = hashString.hash.toString()
			} ?: showProgressBar()
		}

		private fun hideProgressBar() {
			progressBar.visibility = View.GONE
		}

		private fun showProgressBar() {
			progressBar.visibility = View.VISIBLE
		}
	}

	companion object {
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HashString>() {
			override fun areItemsTheSame(oldItem: HashString, newItem: HashString): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: HashString, newItem: HashString): Boolean {
				return oldItem == newItem
			}
		}
	}
}