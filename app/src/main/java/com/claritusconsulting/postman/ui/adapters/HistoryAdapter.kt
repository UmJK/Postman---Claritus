package com.claritusconsulting.postman.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.claritusconsulting.postman.R
import com.claritusconsulting.postman.data.ApiRequest
import kotlinx.android.synthetic.main.history_item.view.*

class HistoryAdapter(private val items: List<ApiRequest>, val context: Context?, private val clickListener: (ApiRequest)->Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.methodText.text = items[position].method
        holder.urlText.text = items[position].url
        holder.containerView.setOnClickListener{clickListener(items[position])}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item, parent, false))
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val methodText:TextView = view.methodText
    val urlText:TextView = view.urlText
    val containerView = view
}