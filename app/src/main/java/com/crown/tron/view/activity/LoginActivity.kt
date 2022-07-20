package com.crown.tron.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.LoginController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.User

class LoginActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var loading: Loading
  private lateinit var request: RequestQueue
  private lateinit var move: Intent
  private lateinit var username: EditText
  private lateinit var password: EditText
  private lateinit var login: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    username = findViewById(R.id.editTextUsername)
    password = findViewById(R.id.editTextPassword)
    login = findViewById(R.id.buttonLogin)

    password.setOnEditorActionListener { _, _, _ ->
      login.performClick()
      true
    }

    login.setOnClickListener {
      if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
        doRequestPermission()
      } else {
        loading.openDialog()
        LoginController(request).invoke(username.text.toString(), password.text.toString()).call({
          user.setString("token", it.getString("token"))
          user.setInteger("code", it.getJSONObject("user").getInt("code"))
          user.setString("username", it.getJSONObject("user").getString("username"))
          user.setString("email", it.getJSONObject("user").getString("email"))
          user.setString("name", it.getJSONObject("user").getString("name"))
          user.setString("referral", it.getJSONObject("user").getString("referral"))
          user.setString("address", it.getJSONObject("user").getString("address"))

          loading.closeDialog()
          move = Intent(this, NavigationActivity::class.java)
          startActivity(move)
          finishAffinity()
        }, {
          loading.closeDialog()
          Toast.makeText(this, HandleError(it).result().getString("message"), Toast.LENGTH_LONG).show()
        })
      }
    }
  }

  private fun doRequestPermission() {
    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
  }
}