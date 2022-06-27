package com.crown.tron.controller

import android.content.Context
import com.crown.tron.http.web.Post
import org.json.JSONObject

class LogoutController(private val context: Context) {
  operator fun invoke() = Post(context, "logout", JSONObject())
}