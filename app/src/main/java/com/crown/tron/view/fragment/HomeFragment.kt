package com.crown.tron.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import com.crown.tron.view.activity.LoginActivity
import com.crown.tron.view.activity.TransferActivity
import com.crown.tron.view.activity.LedgerActivity
import com.crown.tron.view.activity.PackageActivity

class HomeFragment : Fragment() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var textViewReferral: TextView
  private lateinit var textViewProgress: TextView
  private lateinit var textViewTarget: TextView
  private lateinit var textViewBalance: TextView
  private lateinit var progressBar: ProgressBar
  private lateinit var buttonCopyReferral: Button
  private lateinit var linearLayoutPackage: LinearLayout
  private lateinit var linearLayoutTransfer: LinearLayout
  private lateinit var linearLayoutLedger: LinearLayout
  private lateinit var imageViewAddress: ImageView
  private lateinit var historyAdapter: HistoryAdapter
  private lateinit var listViewContainer: RecyclerView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)

    user = User(activity!!)
    request = Volley.newRequestQueue(activity!!)
    loading = Loading(activity!!)

    historyAdapter = HistoryAdapter()

    textViewReferral = view.findViewById(R.id.textViewReferral)
    textViewProgress = view.findViewById(R.id.textViewProgress)
    textViewTarget = view.findViewById(R.id.textViewTarget)
    textViewBalance = view.findViewById(R.id.textViewBalance)
    progressBar = view.findViewById(R.id.progressBar)
    buttonCopyReferral = view.findViewById(R.id.buttonCopyReferral)
    linearLayoutPackage = view.findViewById(R.id.linearLayoutPackage)
    linearLayoutTransfer = view.findViewById(R.id.linearLayoutTransfer)
    linearLayoutLedger = view.findViewById(R.id.linearLayoutLedger)
    imageViewAddress = view.findViewById(R.id.imageViewAddress)

    listViewContainer = view.findViewById<RecyclerView?>(R.id.lists_container).apply {
      layoutManager = LinearLayoutManager(activity!!)
      adapter = historyAdapter
    }

    historyAdapter.clear()

    textViewReferral.text = user.getString("referral")

    imageViewAddress.setOnClickListener {
      WalletModal.show(activity!!, user.getString("address"))
    }

    buttonCopyReferral.setOnClickListener {
      val clipboard = activity!!.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
      val clip = ClipData.newPlainText("referral", textViewReferral.text)
      clipboard.setPrimaryClip(clip)
      Toast.makeText(activity!!, "Link Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    linearLayoutPackage.setOnClickListener {
      move = Intent(activity!!, PackageActivity::class.java)
      startActivity(move)
      activity!!.finish()
    }

    linearLayoutTransfer.setOnClickListener {
      move = Intent(activity!!, TransferActivity::class.java)
      startActivity(move)
      activity!!.finish()
    }

    linearLayoutLedger.setOnClickListener {
      move = Intent(activity!!, LedgerActivity::class.java)
      startActivity(move)
      activity!!.finish()
    }

    getDashboard()

    return view
  }

  private fun getDashboard() {
    loading.openDialog()
    DashboardController(request).invoke(user.getString("token")).call({
      textViewBalance.text = it.getJSONObject("tron").getString("balance")
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
        move = Intent(activity!!, LoginActivity::class.java)
        startActivity(move)
        activity!!.finish()
      } else {
        Toast.makeText(activity!!, handleError.getString("message"), Toast.LENGTH_LONG).show()
      }

      loading.closeDialog()
    })
  }
}