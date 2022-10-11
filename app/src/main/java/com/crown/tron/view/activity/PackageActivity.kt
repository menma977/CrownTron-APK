package com.crown.tron.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.adapter.PackageAdapter
import com.crown.tron.controller.PackageController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.Package
import com.crown.tron.model.User

class PackageActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var packageAdapter: PackageAdapter
  private lateinit var listViewContainer: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_package)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    listViewContainer = findViewById(R.id.lists_container)

    packageAdapter = PackageAdapter(this, user.getString("token"))

    listViewContainer = findViewById<RecyclerView?>(R.id.lists_container).apply {
      layoutManager = LinearLayoutManager(this@PackageActivity)
      adapter = packageAdapter
    }

    packageAdapter.clear()

    loading.openDialog()

    PackageController(request).invoke(user.getString("token")).call({
      val list = it.getJSONArray("packages")

      for (i in 0 until list.length()) {
        val item = list.getJSONObject(i)
        packageAdapter.addItem(
          Package(
            item.getInt("id"),
            item.getString("name"),
            item.getString("description"),
            item.getString("target"),
          )
        )
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
    move = Intent(this, NavigationActivity::class.java)
    startActivity(move)
    finish()
  }
}