package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get

class PackageController(private val request: RequestQueue) {

  operator fun invoke(token: String) = Get(
    request,
    "package.index",
    token
  )

  fun buy(id: Int, token: String) = Get(
    request,
    "package.store.${id}",
    token
  )
}