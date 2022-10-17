package com.crown.tron.http.web

import android.util.Log
import com.android.volley.VolleyError
import org.json.JSONObject
import java.nio.charset.Charset

class HandleError(private val error: VolleyError) {
  fun result(): JSONObject {
    try {
      val raw = String(error.networkResponse.data, Charset.forName("utf-8"))
      val jsonMessage: JSONObject = if (raw.isNotEmpty()) {
        JSONObject(raw)
      } else {
        JSONObject().put("message", "something when wrong").put("logout", false)
      }
      Log.i("WEB-URL", jsonMessage.toString())
      return when (error.networkResponse.statusCode) {
        401 -> {
          JSONObject().put("message", jsonMessage.getString("message")).put("logout", true)
        }

        else -> {
          try {
            JSONObject().put("message", jsonMessage.getJSONObject("errors").getJSONArray(jsonMessage.getJSONObject("errors").names()!![0].toString())[0]).put("logout", false)
          } catch (e: Exception) {
            JSONObject().put("message", "something when wrong").put("logout", false)
          }
        }
      }
    } catch (e: Exception) {
      return JSONObject().put("message", e.localizedMessage).put("logout", false)
    }
  }
}