package com.crown.tron.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.TronController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.User

class TransferActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var wallet: TextView
  private lateinit var balance: TextView
  private lateinit var walletText: EditText
  private lateinit var balanceText: EditText
  private lateinit var send: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_transfer)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    wallet = findViewById(R.id.textViewWallet)
    balance = findViewById(R.id.textViewBalance)
    walletText = findViewById(R.id.editTextWallet)
    balanceText = findViewById(R.id.editTextBalance)
    send = findViewById(R.id.buttonSend)

    loading.openDialog()

    TronController(request).index(user.getString("token")).call({
      wallet.text = it.getString("address")
      val localBalance = it.getString("balance").toString() + " TRX"
      balance.text = localBalance
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
        wallet.text = "-"
        balance.text = "0 TRX"
      }
      loading.closeDialog()
    })

    wallet.setOnClickListener {
      val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      val clip = ClipData.newPlainText("referral", wallet.text.toString())
      clipboard.setPrimaryClip(clip)
      Toast.makeText(this, "Wallet Copied to clipboard", Toast.LENGTH_LONG).show()
    }

    send.setOnClickListener {
      loading.openDialog()
      val to = walletText.text.toString()
      val amount = balanceText.text.toString()
      TronController(request).store(user.getString("token"), to, amount).call({
        Toast.makeText(this, "the transaction is being processed please wait a moment", Toast.LENGTH_LONG).show()
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
  }

  override fun onBackPressed() {
    move = Intent(this, HomeActivity::class.java)
    startActivity(move)
    finish()
  }
}