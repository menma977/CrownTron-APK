package com.crown.tron.controller

import android.content.Context
import com.crown.tron.http.web.Post
import org.json.JSONObject

class LoginController(private val context: Context) {
  operator fun invoke(username: String, password: String) = Post(
    context,
    "login",
    JSONObject()
      .put("username", username)
      .put("password", password)
  )
}