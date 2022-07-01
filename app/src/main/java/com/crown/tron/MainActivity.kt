package com.crown.tron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.controller.ValidateController
import com.crown.tron.model.User
import com.crown.tron.view.HomeActivity
import com.crown.tron.view.LoginActivity

class MainActivity : AppCompatActivity() {
  private lateinit var request: RequestQueue
  private lateinit var move: Intent
  private lateinit var user: User

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    user = User(this)
    request = Volley.newRequestQueue(this)

    Log.d("MainActivity", user.getString("token"))

    if (user.findId("token")) {
      ValidateController(request).invoke(user.getString("token")).call({
        if (it.getBoolean("active")) {
          move = Intent(applicationContext, HomeActivity::class.java)
          startActivity(move)
          finish()
        } else {
          move = Intent(applicationContext, LoginActivity::class.java)
          startActivity(move)
          finish()
        }
      }, {
        move = Intent(applicationContext, LoginActivity::class.java)
        startActivity(move)
        finish()
      })
    } else {
      move = Intent(applicationContext, LoginActivity::class.java)
      startActivity(move)
      finish()
    }
  }
}