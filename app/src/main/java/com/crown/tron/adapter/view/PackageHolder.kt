package com.crown.tron.adapter.view

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PackageHolder(layout: View) : RecyclerView.ViewHolder(layout) {
  val name: TextView = layout.findViewById(com.crown.tron.R.id.name)
  val profit: TextView = layout.findViewById(com.crown.tron.R.id.profit)
  val description: TextView = layout.findViewById(com.crown.tron.R.id.description)
  val maxProfit: TextView = layout.findViewById(com.crown.tron.R.id.maxProfit)
  val buttonBuy: Button = layout.findViewById(com.crown.tron.R.id.buttonBuy)
}