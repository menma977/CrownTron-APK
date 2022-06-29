package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get

class ValidateController(private val request: RequestQueue) {
  operator fun invoke(token: String) = Get(
    request,
    "validate",
    token
  )
}