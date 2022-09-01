package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get
import com.crown.tron.http.web.Post
import org.json.JSONObject

class UserController(private val request: RequestQueue) {

  fun create(token: String) = Get(
    request,
    "user.create",
    token
  )

  operator fun invoke(name: String, username: String, email: String, password: String, password_confirmation: String, position: String, token: String) = Post(
    request,
    "user.store",
    JSONObject()
      .put("name", name)
      .put("username", username)
      .put("email", email)
      .put("password", password)
      .put("password_confirmation", password_confirmation)
      .put("position", position),
    token
  )
}