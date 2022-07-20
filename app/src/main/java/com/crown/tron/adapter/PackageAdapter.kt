package com.crown.tron.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.adapter.view.PackageHolder
import com.crown.tron.controller.PackageController
import com.crown.tron.http.web.HandleError
import com.crown.tron.model.Package as ModelPackage

class PackageAdapter(private var context: Context, private var token: String) : RecyclerView.Adapter<PackageHolder>() {
  private val dataSet = ArrayList<com.crown.tron.model.Package>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageHolder {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_package, parent, false)
    return PackageHolder(layout)
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onBindViewHolder(holder: PackageHolder, position: Int) {
    holder.name.text = dataSet[position].name
    holder.profit.text = dataSet[position].profit
    holder.description.text = dataSet[position].description
    holder.maxProfit.text = dataSet[position].maxProfit
    holder.buttonBuy.setOnClickListener {
      val request = Volley.newRequestQueue(context)
      PackageController(request).buy(dataSet[position].id, token).call({
        Toast.makeText(context, it.getString("message"), Toast.LENGTH_LONG).show()
      }, {
        val handleError = HandleError(it).result()
        Toast.makeText(context, handleError.getString("message"), Toast.LENGTH_LONG).show()
      })
    }
  }

  @SuppressLint("NotifyDataSetChanged")
  fun addItem(item: ModelPackage) {
    dataSet.add(item)
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, dataSet.size)
  }

  @SuppressLint("NotifyDataSetChanged")
  fun clear() {
    dataSet.clear()
    this.notifyDataSetChanged()
    this.notifyItemRangeInserted(0, dataSet.size)
  }
}