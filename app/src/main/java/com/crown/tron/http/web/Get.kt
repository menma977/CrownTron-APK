package com.crown.tron.http.web

import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.crown.tron.model.Url
import org.json.JSONObject

class Get(private var request: RequestQueue, private var targetUrl: String, private var body: JSONObject, private var token: String?, private var withBody: Boolean) {
  constructor(request: RequestQueue, targetUrl: String, token: String) : this(request, targetUrl, JSONObject(), token, false)
  constructor(request: RequestQueue, targetUrl: String) : this(request, targetUrl, JSONObject(), null, false)

  fun call(response: Response.Listener<JSONObject>, error: Response.ErrorListener) {
    val jsonRequest = object : JsonObjectRequest(Method.GET, Url.web(targetUrl), if (withBody) body else null, response, error) {
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

    jsonRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

    request.add(jsonRequest)
  }

  fun callString(response: Response.Listener<String>, error: Response.ErrorListener) {
    val stringRequest = object : StringRequest(Method.GET, Url.web(targetUrl), response, error) {
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

    stringRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

    request.add(stringRequest)
  }
}