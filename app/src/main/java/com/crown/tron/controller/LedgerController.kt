package com.crown.tron.controller

import com.android.volley.RequestQueue
import com.crown.tron.http.web.Get

class LedgerController(private val request: RequestQueue) {

  fun claim(token: String, type: Int) = Get(
    request,
    "ledger.$type.claim",
    token
  )

  fun withdraw(token: String) = Get(
    request,
    "ledger.withdraw",
    token
  )
}