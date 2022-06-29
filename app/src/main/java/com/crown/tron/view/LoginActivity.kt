package com.crown.tron.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.modal.Loading
import com.crown.tron.model.User

class LoginActivity : AppCompatActivity() {
  private lateinit var request: RequestQueue
  private lateinit var move: Intent
  private lateinit var user: User
  private lateinit var loading: Loading
  private lateinit var username: EditText
  private lateinit var password: EditText
  private lateinit var register: TextView
  private lateinit var login: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)
  }
}