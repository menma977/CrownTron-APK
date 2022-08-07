package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get
import com.crown.tron.http.web.Post
import org.json.JSONObject

class PinController(private val request: RequestQueue) {
  fun create(token: String) = Get(
    request,
    "pin.create",
    token
  )

  fun store(token: String, wallet: String, amount: String) = Post(
    request,
    "pin.store",
    JSONObject().put("address", wallet).put("amount", amount),
    token
  )
}