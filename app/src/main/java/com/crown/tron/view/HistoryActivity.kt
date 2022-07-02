package com.crown.tron.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.adapter.HistoryAdapter
import com.crown.tron.controller.HistoryController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.History
import com.crown.tron.model.User

class HistoryActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var loading: Loading
  private lateinit var request: RequestQueue
  private lateinit var move: Intent
  private lateinit var historyAdapter: HistoryAdapter
  private lateinit var listViewContainer: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_history)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    historyAdapter = HistoryAdapter()

    listViewContainer = findViewById<RecyclerView?>(R.id.lists_container).apply {
      layoutManager = LinearLayoutManager(this@HistoryActivity)
      adapter = historyAdapter
    }

    historyAdapter.clear()

    getHistory()
  }

  private fun getHistory() {
    historyAdapter.clear()
    loading.openDialog()

    HistoryController(request).invoke(user.getString("token")).call({
      val list = it.getJSONArray("history")

      for (i in 0 until list.length()) {
        val item = list.getJSONObject(i)
        historyAdapter.addItem(History(
          item.getString("description"),
          item.getString("created_at"),
        ))
      }

      loading.closeDialog()
    }, {
      val handleError = HandleError(it).result()
      if (handleError.getBoolean("logout")) {
        user.clear()
        move = Intent(this, LoginActivity::class.java)
        startActivity(move)
        finish()
      } else {
        Toast.makeText(this, handleError.getString("message"), Toast.LENGTH_LONG).show()
      }
      loading.closeDialog()
    })
  }

  override fun onBackPressed() {
    move = Intent(this, HomeActivity::class.java)
    startActivity(move)
    finishAffinity()
  }
}