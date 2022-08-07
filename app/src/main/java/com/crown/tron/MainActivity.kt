package com.crown.tron

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.controller.ValidateController
import com.crown.tron.model.User
import com.crown.tron.view.activity.LoginActivity
import com.crown.tron.view.activity.NavigationActivity

class MainActivity : AppCompatActivity() {
  private lateinit var request: RequestQueue
  private lateinit var move: Intent
  private lateinit var user: User

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    user = User(this)
    request = Volley.newRequestQueue(this)

    if (user.findId("token")) {
      ValidateController(request).invoke(user.getString("token")).call({
        if (it.getBoolean("active")) {
          move = Intent(applicationContext, NavigationActivity::class.java)
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