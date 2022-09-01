package com.crown.tron.view.activity

import android.annotation.SuppressLint
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
import com.budiyev.android.codescanner.*
import com.crown.tron.MainActivity
import com.crown.tron.R
import com.crown.tron.controller.PinController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.User

class TransferPinActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var frameScanner: CodeScannerView
  private lateinit var codeScanner: CodeScanner
  private lateinit var wallet: TextView
  private lateinit var balance: TextView
  private lateinit var walletText: EditText
  private lateinit var balanceText: EditText
  private lateinit var send: Button

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_transfer_pin)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    frameScanner = findViewById(R.id.frameLayoutScanner)
    wallet = findViewById(R.id.textViewWallet)
    balance = findViewById(R.id.textViewBalance)
    walletText = findViewById(R.id.editTextWallet)
    balanceText = findViewById(R.id.editTextBalance)
    send = findViewById(R.id.buttonSend)

    loading.openDialog()

    startScanning()

    wallet.text = user.getString("pin_address")

    PinController(request).create(user.getString("token")).call({
      val localBalance = it.getString("pin_value").toString() + " PIN"
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
        balance.text = "0 PIN"
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
      PinController(request).store(user.getString("token"), to, amount).call({
        Toast.makeText(this, "the transaction is being processed please wait a moment", Toast.LENGTH_LONG).show()
        loading.closeDialog()
        finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
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

  private fun startScanning() {
    val scannerView: CodeScannerView = findViewById(R.id.frameLayoutScanner)
    codeScanner = CodeScanner(this, scannerView)
    codeScanner.camera = CodeScanner.CAMERA_BACK
    codeScanner.formats = CodeScanner.ALL_FORMATS
    codeScanner.autoFocusMode = AutoFocusMode.SAFE
    codeScanner.scanMode = ScanMode.SINGLE
    codeScanner.isAutoFocusEnabled = true
    codeScanner.isFlashEnabled = false

    codeScanner.decodeCallback = DecodeCallback {
      runOnUiThread {
        walletText.setText(it.text)
      }
    }
    codeScanner.errorCallback = ErrorCallback {
      runOnUiThread {
        Toast.makeText(
          this, "Camera initialization error: ${it.message}",
          Toast.LENGTH_LONG
        ).show()
      }
    }

    scannerView.setOnClickListener {
      codeScanner.startPreview()
    }
  }

  override fun onResume() {
    super.onResume()
    if (::codeScanner.isInitialized) {
      codeScanner.startPreview()
    }
  }

  override fun onPause() {
    if (::codeScanner.isInitialized) {
      codeScanner.releaseResources()
    }
    super.onPause()
  }

  override fun onBackPressed() {
    move = Intent(this, NavigationActivity::class.java)
    startActivity(move)
    finish()
  }
}