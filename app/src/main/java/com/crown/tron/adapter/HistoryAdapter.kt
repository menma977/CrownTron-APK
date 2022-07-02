package com.crown.tron.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crown.tron.R
import com.crown.tron.adapter.view.HistoryHolder
import com.crown.tron.model.History

class HistoryAdapter : RecyclerView.Adapter<HistoryHolder>() {
  private val dataSet = ArrayList<History>()

  init {
    dataSet.add(History("Description", "AT"))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_history, parent, false)
    return HistoryHolder(layout)
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
    holder.description.text = dataSet[position].description
    holder.date.text = dataSet[position].date
  }
  @SuppressLint("NotifyDataSetChanged")
  fun addItem(item: History) {
    dataSet.add(1, item)
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, dataSet.size)
  }

  @SuppressLint("NotifyDataSetChanged")
  fun clear() {
    dataSet.clear()
    dataSet.add(History("Description", "AT"))
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, dataSet.size)
  }
}