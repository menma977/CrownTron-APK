package com.crown.tron.http.web

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.crown.tron.model.Url
import org.json.JSONObject

class Post(private var request: RequestQueue, private var targetUrl: String, private var body: JSONObject, private var token: String? = null) {
  constructor(request: RequestQueue, targetUrl: String, body: JSONObject) : this(request, targetUrl, body, null)

  fun call(response: Response.Listener<JSONObject>, error: Response.ErrorListener) {
    val jsonRequest = object : JsonObjectRequest(Method.POST, Url.web(targetUrl), body, response, error) {
      override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        if (!token.isNullOrEmpty()) {
          headers["Authorization"] = "Bearer ${token!!}"
        }
        headers["X-Requested-With"] = "XMLHttpRequest"
        headers["Content-Type"] = "application/json; charset=utf-8"
        return headers
      }
    }

    request.add(jsonRequest)
  }
}