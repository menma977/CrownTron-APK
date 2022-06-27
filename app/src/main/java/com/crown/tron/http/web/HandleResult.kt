package com.crown.tron.http.web

import org.json.JSONObject

class HandleResult(private val json: JSONObject) {
  fun result(): JSONObject {
    return try {
      when {
        json.has("message") -> {
          JSONObject().put("data", json.getString("message")).put("logout", false)
        }
        else -> {
          JSONObject().put("data", json).put("logout", false)
        }
      }
    } catch (e: Exception) {
      JSONObject().put("message", e.localizedMessage)
    }
  }
}