package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get
import com.crown.tron.http.web.Post
import org.json.JSONObject

class TronController(private val request: RequestQueue) {
  fun index(token: String) = Get(
    request,
    "tron.index",
    token
  )

  fun store(token: String, wallet: String, amount: String, type: Int) = Post(
    request,
    "tron.store",
    JSONObject().put("address", wallet).put("amount", amount).put("type", type),
    token
  )
}