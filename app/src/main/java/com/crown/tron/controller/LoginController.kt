package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Post
import org.json.JSONObject

class LoginController(private val request: RequestQueue) {
  operator fun invoke(username: String, password: String) = Post(
    request,
    "login",
    JSONObject()
      .put("username", username)
      .put("password", password)
  )
}