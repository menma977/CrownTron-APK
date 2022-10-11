package com.crown.tron.model

import com.crown.tron.BuildConfig

object Url {
  /**
   * @param target String
   * @return String
   */
  fun web(target: String, toApi: Boolean = true): String {
    var domain = "https://crowntechsolution.com/"
    if (BuildConfig.DEBUG) {
      domain = "http://10.0.2.2:8000/"
    }

    val child: String = if (toApi) {
      "api/${target.replace(".", "/")}"
    } else {
      target.replace(".", "/")
    }

    return domain + child
  }
}