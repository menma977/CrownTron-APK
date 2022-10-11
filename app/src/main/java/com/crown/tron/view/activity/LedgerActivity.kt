package com.crown.tron.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.adapter.LedgerAdapter
import com.crown.tron.controller.LedgerController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.Ledger
import com.crown.tron.model.User

class LedgerActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var textViewProfitBalance: TextView
  private lateinit var textViewProfitPairing: TextView
  private lateinit var textViewProfitReward: TextView
  private lateinit var buttonProfitBalance: Button
  private lateinit var buttonProfitPairing: Button
  private lateinit var ledgerAdapter: LedgerAdapter
  private lateinit var listViewContainer: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_ledger)

    user = User(this)
    request = Volley.newRequestQueue(this)
    loading = Loading(this)

    textViewProfitBalance = findViewById(R.id.textViewProfitBalance)
    textViewProfitPairing = findViewById(R.id.textViewProfitPairing)
    textViewProfitReward = findViewById(R.id.textViewRewardPairing)

    buttonProfitBalance = findViewById(R.id.buttonClaimProfitBalance)
    buttonProfitPairing = findViewById(R.id.buttonClaimProfitPairing)

    listViewContainer = findViewById(R.id.lists_container)

    ledgerAdapter = LedgerAdapter(this)

    listViewContainer = findViewById<RecyclerView?>(R.id.lists_container).apply {
      layoutManager = LinearLayoutManager(this@LedgerActivity)
      adapter = ledgerAdapter
    }

    ledgerAdapter.clear()

    buttonProfitBalance.setOnClickListener {
      loading.openDialog()
      LedgerController(request).withdraw(user.getString("token")).call({
        Toast.makeText(this, it.getString("message"), Toast.LENGTH_SHORT).show()
        setLedger()
      }, {
        val handleError = HandleError(it).result()
        if (handleError.getBoolean("logout")) {
          user.clear()
          move = Intent(this, LoginActivity::class.java)
          startActivity(move)
          finish()
        } else {
          Toast.makeText(this, handleError.getString("message"), Toast.LENGTH_SHORT).show()
        }

        loading.closeDialog()
      })
    }

    buttonProfitPairing.setOnClickListener {
      loading.openDialog()
      LedgerController(request).claim(user.getString("token"), 1).call({
        Toast.makeText(this, it.getString("message"), Toast.LENGTH_SHORT).show()
        setLedger()
      }, {
        val handleError = HandleError(it).result()
        if (handleError.getBoolean("logout")) {
          user.clear()
          move = Intent(this, LoginActivity::class.java)
          startActivity(move)
          finish()
        } else {
          Toast.makeText(this, handleError.getString("message"), Toast.LENGTH_SHORT).show()
        }

        loading.closeDialog()
      })
    }

    loading.openDialog()
    setLedger()
  }

  private fun setLedger() {
    LedgerController(request).invoke(user.getString("token")).call({
      textViewProfitBalance.text = it.getJSONObject("ledger").getString("balance")
      textViewProfitPairing.text = it.getJSONObject("ledger").getString("profit")
      textViewProfitReward.text = it.getJSONObject("ledger").getString("reward")

      val list = it.getJSONObject("ledger").getJSONArray("all")

      ledgerAdapter.clear()

      for (i in list.length() - 1 downTo 0) {
        val item = list.getJSONObject(i)
        ledgerAdapter.addItem(
          Ledger(
            item.getString("description"),
            item.getString("in"),
            item.getString("out"),
            item.getString("at")
          )
        )
      }

      loading.closeDialog()
    }, {
      val handleError = HandleError(it).result()
      if (handleError.getBoolean("logout")) {
        user.clear()
        move = Intent(this, LoginActivity::class.java)
        startActivity(move)
        finish()
      } else {
        Toast.makeText(this, handleError.getString("message"), Toast.LENGTH_LONG).show()
      }

      loading.closeDialog()
    })
  }

  override fun onBackPressed() {
    move = Intent(this, NavigationActivity::class.java)
    startActivity(move)
    finishAffinity()
  }
}