package com.qbikkx.stringrandomizer.stringsscreen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qbikkx.data.hashstring.HashString
import com.qbikkx.stringrandomizer.R
import kotterknife.bindView

/**
 * Created by qbikkx on 16.03.18.
 */
class StringsAdapter(private var strings: List<HashString>) :
        RecyclerView.Adapter<StringsAdapter.StringsViewHolder>() {

    override fun onBindViewHolder(holder: StringsViewHolder?, position: Int) {
        holder?.bind(strings[position])
    }

    override fun getItemCount(): Int = strings.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StringsViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_string, parent, false)
        return StringsViewHolder(view)
    }

    class StringsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val value: TextView by bindView(R.id.item_string_value)
        val hash: TextView by bindView(R.id.item_string_hash)

        fun bind(hashString: HashString) {
            value.text = hashString.string
            hash.text = hashString.hash.toString()
        }
    }

    fun setData(strings: List<HashString>) {
        this.strings = strings
        notifyDataSetChanged()
    }
}