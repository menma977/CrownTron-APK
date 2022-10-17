package com.crown.tron.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.NetworkController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.Url
import com.crown.tron.model.User
import com.crown.tron.view.activity.LoginActivity

class NetworkFragment : Fragment() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var webContent: WebView
  private lateinit var move: Intent

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_network, container, false)

    user = User(requireActivity())
    request = Volley.newRequestQueue(requireActivity())
    loading = Loading(requireActivity())

    webContent = view.findViewById(R.id.webViewContent)

    loading.openDialog()
    NetworkController(request).invoke(user.getInteger("code"), user.getString("token")).callString({
      webContent.removeAllViews()
      webContent.webViewClient = WebViewClient()
      webContent.webChromeClient = WebChromeClient()
      webContent.settings.javaScriptEnabled = true
      webContent.settings.domStorageEnabled = true
      webContent.settings.javaScriptCanOpenWindowsAutomatically = true
      webContent.settings.loadWithOverviewMode = true
      webContent.loadData(it, "text/html", "UTF-8")

      val headers = HashMap<String, String>()
      headers["Authorization"] = "Bearer ${user.getString("token")}"
      headers["X-Requested-With"] = "XMLHttpRequest"
      headers["Content-Type"] = "application/json; charset=utf-8"

      webContent.webViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? {
          CookieManager.getInstance().removeAllCookies(null)
          return super.shouldInterceptRequest(view, request)
        }
      }
      webContent.loadDataWithBaseURL(Url.web("network.${user.getInteger("code")}.${user.getInteger("code")}", true), it, "text/html", "UTF-8", null)
      loading.closeDialog()
    }, {
      val handleError = HandleError(it).result()
      if (handleError.getBoolean("logout")) {
        user.clear()
        move = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(move)
        requireActivity().finishAffinity()
      } else {
        Toast.makeText(requireActivity(), handleError.getString("message"), Toast.LENGTH_LONG).show()
      }

      loading.closeDialog()
    })

    return view
  }
}