package com.crown.tron.adapter.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryHolder(layout: View) : RecyclerView.ViewHolder(layout) {
  val description: TextView = layout.findViewById(com.crown.tron.R.id.description)
  val date: TextView = layout.findViewById(com.crown.tron.R.id.date)
}