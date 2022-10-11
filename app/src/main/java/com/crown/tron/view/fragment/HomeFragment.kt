package com.crown.tron.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.adapter.HistoryAdapter
import com.crown.tron.controller.DashboardController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.modal.WalletModal
import com.crown.tron.model.History
import com.crown.tron.model.User
import com.crown.tron.view.activity.*

class HomeFragment : Fragment() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var textViewProgress: TextView
  private lateinit var textViewTarget: TextView
  private lateinit var textViewBalance: TextView
  private lateinit var textViewBalanceUsdt: TextView
  private lateinit var textViewPin: TextView
  private lateinit var progressBar: ProgressBar
  private lateinit var linearLayoutPackage: LinearLayout
  private lateinit var linearLayoutTransfer: LinearLayout
  private lateinit var linearLayoutTransferUsdt: LinearLayout
  private lateinit var linearLayoutTransferPin: LinearLayout
  private lateinit var linearLayoutLedger: LinearLayout
  private lateinit var imageViewAddress: ImageView
  private lateinit var imageViewAddressUsdt: ImageView
  private lateinit var imageViewPinAddress: ImageView
  private lateinit var historyAdapter: HistoryAdapter
  private lateinit var listViewContainer: RecyclerView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)

    user = User(requireActivity())
    request = Volley.newRequestQueue(requireActivity())
    loading = Loading(requireActivity())

    historyAdapter = HistoryAdapter(requireContext())
    textViewProgress = view.findViewById(R.id.textViewProgress)
    textViewTarget = view.findViewById(R.id.textViewTarget)
    textViewBalance = view.findViewById(R.id.textViewBalance)
    textViewBalanceUsdt = view.findViewById(R.id.textViewBalanceUsdt)
    textViewPin = view.findViewById(R.id.textViewPin)
    progressBar = view.findViewById(R.id.progressBar)
    linearLayoutPackage = view.findViewById(R.id.linearLayoutPackage)
    linearLayoutTransfer = view.findViewById(R.id.linearLayoutTransfer)
    linearLayoutTransferUsdt = view.findViewById(R.id.linearLayoutTransferUsdt)
    linearLayoutTransferPin = view.findViewById(R.id.linearLayoutTransferPin)
    linearLayoutLedger = view.findViewById(R.id.linearLayoutLedger)
    imageViewAddress = view.findViewById(R.id.imageViewAddress)
    imageViewAddressUsdt = view.findViewById(R.id.imageViewAddressUsdt)
    imageViewPinAddress = view.findViewById(R.id.imageViewPinAddress)

    listViewContainer = view.findViewById<RecyclerView?>(R.id.lists_container).apply {
      layoutManager = LinearLayoutManager(requireActivity())
      adapter = historyAdapter
    }

    historyAdapter.clear()

    imageViewAddress.setOnClickListener {
      WalletModal.show(requireActivity(), user.getString("address"), "Tron Address")
    }

    imageViewAddressUsdt.setOnClickListener {
      WalletModal.show(requireActivity(), user.getString("address"), "Usdt/Tron Address")
    }

    imageViewPinAddress.setOnClickListener {
      WalletModal.show(requireActivity(), user.getString("pin_address"), "Pin Address")
    }

    linearLayoutPackage.setOnClickListener {
      move = Intent(requireActivity(), PackageActivity::class.java)
      startActivity(move)
      requireActivity().finish()
    }

    linearLayoutTransfer.setOnClickListener {
      move = Intent(requireActivity(), TransferActivity::class.java)
      move.putExtra("type", 0)
      startActivity(move)
      requireActivity().finish()
    }

    linearLayoutTransferUsdt.setOnClickListener {
      move = Intent(requireActivity(), TransferActivity::class.java)
      move.putExtra("type", 1)
      startActivity(move)
      requireActivity().finish()
    }

    linearLayoutTransferPin.setOnClickListener {
      move = Intent(requireActivity(), TransferPinActivity::class.java)
      startActivity(move)
      requireActivity().finish()
    }

    linearLayoutLedger.setOnClickListener {
      move = Intent(requireActivity(), LedgerActivity::class.java)
      startActivity(move)
      requireActivity().finish()
    }

    getDashboard()

    return view
  }

  private fun getDashboard() {
    loading.openDialog()
    DashboardController(request).invoke(user.getString("token")).call({
      textViewBalance.text = it.getJSONObject("balance").getString("tron")
      textViewBalanceUsdt.text = it.getJSONObject("balance").getString("usdt")
      textViewPin.text = it.getJSONObject("pin").getString("total")
      textViewTarget.text = it.getJSONObject("package").getString("target")
      textViewProgress.text = it.getJSONObject("package").getString("progress")
      progressBar.progress = it.getJSONObject("package").getInt("progress")

      val list = it.getJSONArray("history")

      if (list.length() <= 0) {
        History(
          "No Data Available",
          "-",
        )
      }

      for (i in list.length() - 1 downTo 0) {
        val item = list.getJSONObject(i)
        historyAdapter.addItem(
          History(
            item.getString("description"),
            item.getString("at"),
          )
        )
      }

      loading.closeDialog()
    }, {
      val handleError = HandleError(it).result()
      if (handleError.getBoolean("logout")) {
        user.clear()
        move = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(move)
        requireActivity().finishAffinity()
      } else {
        Toast.makeText(requireActivity(), handleError.getString("message"), Toast.LENGTH_LONG).show()
      }

      loading.closeDialog()
    })
  }
}