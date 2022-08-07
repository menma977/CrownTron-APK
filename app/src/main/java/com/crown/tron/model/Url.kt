package com.crown.tron.model

object Url {
  /**
   * @param target String
   * @return String
   */
  fun web(target: String, toApi: Boolean = true): String {
    val domain = "https://crowntron.com/"
//    val domain = "http://10.0.2.2:8000/"
    val child: String = if (toApi) {
      "api/${target.replace(".", "/")}"
    } else {
      target.replace(".", "/")
    }

    return domain + child
  }
}