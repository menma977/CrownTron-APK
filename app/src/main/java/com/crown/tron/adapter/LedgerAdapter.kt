package com.crown.tron.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crown.tron.R
import com.crown.tron.adapter.view.LedgerHolder
import com.crown.tron.model.Ledger

class LedgerAdapter : RecyclerView.Adapter<LedgerHolder>() {
  private val dataSet = ArrayList<Ledger>()

  init {
    dataSet.add(Ledger("Description", "Income", "Outcome", "AT"))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerHolder {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_ledger, parent, false)
    return LedgerHolder(layout)
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onBindViewHolder(holder: LedgerHolder, position: Int) {
    holder.description.text = dataSet[position].description
    holder.income.text = dataSet[position].income
    holder.outcome.text = dataSet[position].outcome
    holder.date.text = dataSet[position].date
  }
  @SuppressLint("NotifyDataSetChanged")
  fun addItem(item: Ledger) {
    dataSet.add(1, item)
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, dataSet.size)
  }

  @SuppressLint("NotifyDataSetChanged")
  fun clear() {
    dataSet.clear()
    dataSet.add(Ledger("Description", "Income", "Outcome", "AT"))
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, dataSet.size)
  }
}