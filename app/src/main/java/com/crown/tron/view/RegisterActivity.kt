package com.crown.tron.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.UserController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.User

class RegisterActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var name: EditText
  private lateinit var username: EditText
  private lateinit var email: EditText
  private lateinit var password: EditText
  private lateinit var confirm: EditText
  private lateinit var positionLeft: RadioButton
  private lateinit var positionRight: RadioButton
  private lateinit var send: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    user = User(this)
    request = Volley.newRequestQueue(this)
    loading = Loading(this)

    name = findViewById(R.id.editTextName)
    username = findViewById(R.id.editTextUsername)
    email = findViewById(R.id.editTextEmail)
    password = findViewById(R.id.editTextPassword)
    confirm = findViewById(R.id.editTextPasswordConfirmation)
    positionLeft = findViewById(R.id.radioButtonLeft)
    positionRight = findViewById(R.id.radioButtonRight)
    send = findViewById(R.id.buttonSend)

    send.setOnClickListener {
      loading.openDialog()
      val position = if (positionLeft.isChecked) "left" else "right"
      UserController(request).invoke(
        name.text.toString(),
        username.text.toString(),
        email.text.toString(),
        password.text.toString(),
        confirm.text.toString(),
        position,
        user.getString("token")
      ).call({
        Log.i("RegisterActivity", it.toString())
        Toast.makeText(this, it.getString("message"), Toast.LENGTH_LONG).show()
        loading.closeDialog()
      }, {
        val handleError = HandleError(it).result()
        Log.i("RegisterActivity E", handleError.toString())
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