package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get

class NetworkController(private val request: RequestQueue) {

  operator fun invoke(id: Int, token: String) = Get(
    request,
    "network.${id}.${id}",
    token
  )
}