package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Post
import org.json.JSONObject

class LogoutController(private val request: RequestQueue) {
  operator fun invoke(token: String) = Post(request, "logout", JSONObject(), token)
}