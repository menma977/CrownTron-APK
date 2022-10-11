package com.crown.tron.adapter.view

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PackageHolder(layout: View) : RecyclerView.ViewHolder(layout) {
  val name: TextView = layout.findViewById(com.crown.tron.R.id.name)
  val maxProfit: TextView = layout.findViewById(com.crown.tron.R.id.maxProfit)
  val description: TextView = layout.findViewById(com.crown.tron.R.id.description)
  val buttonBuy: Button = layout.findViewById(com.crown.tron.R.id.buttonBuy)
}