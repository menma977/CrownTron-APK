package com.crown.tron.adapter.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LedgerHolder(layout: View) : RecyclerView.ViewHolder(layout) {
  val description: TextView = layout.findViewById(com.crown.tron.R.id.description)
  val income: TextView = layout.findViewById(com.crown.tron.R.id.income)
  val outcome: TextView = layout.findViewById(com.crown.tron.R.id.outcome)
  val date: TextView = layout.findViewById(com.crown.tron.R.id.date)
}