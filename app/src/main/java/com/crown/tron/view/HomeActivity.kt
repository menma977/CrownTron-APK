package com.crown.tron.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.DashboardController
import com.crown.tron.controller.LogoutController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.User

class HomeActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var textViewUsername: TextView
  private lateinit var textViewReferral: TextView
  private lateinit var textViewProfitBalance: TextView
  private lateinit var textViewProfitPackage: TextView
  private lateinit var textViewProfitPairing: TextView
  private lateinit var textViewTotalProfit: TextView
  private lateinit var textViewTotalProfitLeft: TextView
  private lateinit var textViewTotalProfitRight: TextView
  private lateinit var textViewProfitLeft: TextView
  private lateinit var textViewProfitRight: TextView
  private lateinit var textViewUserLeft: TextView
  private lateinit var textViewUserRight: TextView
  private lateinit var buttonLogout: ImageButton
  private lateinit var buttonCopy: Button
  private lateinit var linearLayoutHistory: LinearLayout
  private lateinit var linearLayoutPackage: LinearLayout
  private lateinit var linearLayoutLedger: LinearLayout
  private lateinit var linearLayoutTransfer: LinearLayout
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    textViewUsername = findViewById(R.id.textViewUsername)
    textViewReferral = findViewById(R.id.textViewLinkReferral)
    textViewProfitBalance = findViewById(R.id.textViewProfitBalance)
    textViewProfitPackage = findViewById(R.id.textViewProfitPackage)
    textViewProfitPairing = findViewById(R.id.textViewProfitPairing)
    textViewTotalProfit = findViewById(R.id.textViewTotalProfit)
    textViewTotalProfitLeft = findViewById(R.id.textViewTotalProfitLeft)
    textViewTotalProfitRight = findViewById(R.id.textViewTotalProfitRight)
    textViewProfitLeft = findViewById(R.id.textViewProfitLeft)
    textViewProfitRight = findViewById(R.id.textViewProfitRight)
    textViewUserLeft = findViewById(R.id.textViewUserLeft)
    textViewUserRight = findViewById(R.id.textViewUserRight)

    buttonLogout = findViewById(R.id.imageButtonLogout)
    buttonCopy = findViewById(R.id.buttonCopyReferral)

    linearLayoutHistory = findViewById(R.id.linearLayoutHistory)
    linearLayoutPackage = findViewById(R.id.linearLayoutPackage)
    linearLayoutLedger = findViewById(R.id.linearLayoutLedger)
    linearLayoutTransfer = findViewById(R.id.linearLayoutTransfer)

    textViewUsername.text = user.getString("username")
    textViewReferral.text = user.getString("referral")

    DashboardController(request).invoke(user.getString("token")).call({
      textViewProfitBalance.text = it.getJSONObject("ledger").getString("balance")
      textViewProfitPackage.text = it.getJSONObject("ledger").getString("package")
      textViewProfitPairing.text = it.getJSONObject("ledger").getString("profit")
      textViewTotalProfit.text = it.getJSONObject("binary").getString("total")
      textViewTotalProfitLeft.text = it.getJSONObject("binary").getString("total_profit_left")
      textViewTotalProfitRight.text = it.getJSONObject("binary").getString("total_profit_right")
      textViewProfitLeft.text = it.getJSONObject("binary").getString("profit_left")
      textViewProfitRight.text = it.getJSONObject("binary").getString("profit_right")
      textViewUserLeft.text = it.getJSONObject("binary").getString("user_left")
      textViewUserRight.text = it.getJSONObject("binary").getString("user_right")
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
    })

    buttonLogout.setOnClickListener {
      user.clear()
      move = Intent(this, LoginActivity::class.java)
      LogoutController(request).invoke(user.getString("token")).call({}, {})
      startActivity(move)
      finish()
    }

    buttonCopy.setOnClickListener {
      val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      val clip = ClipData.newPlainText("referral", textViewReferral.text)
      clipboard.setPrimaryClip(clip)
      Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_LONG).show()
    }


  }
}