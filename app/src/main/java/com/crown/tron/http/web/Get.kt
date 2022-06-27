package com.crown.tron.http.web

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.crown.tron.model.Url
import org.json.JSONObject

class Get(private var context: Context, private var targetUrl: String, private var body: JSONObject, private var token: String?, private var withBody: Boolean) {
  constructor(context: Context, targetUrl: String, token: String) : this(context, targetUrl, JSONObject(), token, false)
  constructor(context: Context, targetUrl: String) : this(context, targetUrl, JSONObject(), null, false)

  fun call(response: Response.Listener<JSONObject>, error: Response.ErrorListener) {
    val request = Volley.newRequestQueue(context)
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

    request.add(jsonRequest)
  }

  fun callString(response: Response.Listener<String>, error: Response.ErrorListener) {
    val request = Volley.newRequestQueue(context)
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

    request.add(stringRequest)
  }
}