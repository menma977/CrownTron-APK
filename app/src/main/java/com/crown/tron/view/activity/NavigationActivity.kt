package com.crown.tron.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.LogoutController
import com.crown.tron.modal.Loading
import com.crown.tron.model.User
import com.crown.tron.view.fragment.AddUserFragment
import com.crown.tron.view.fragment.HomeFragment
import com.crown.tron.view.fragment.NetworkFragment

class NavigationActivity : AppCompatActivity() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var textViewUsername: TextView
  private lateinit var imageButtonLogout: ImageButton
  private lateinit var linearLayoutHome: LinearLayout
  private lateinit var linearLayoutNetwork: LinearLayout
  private lateinit var linearLayoutAddUser: LinearLayout

  private var fragmentPosition: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)

    user = User(this)
    loading = Loading(this)
    request = Volley.newRequestQueue(this)

    textViewUsername = findViewById(R.id.textViewUsername)
    imageButtonLogout = findViewById(R.id.imageButtonLogout)

    linearLayoutHome = findViewById(R.id.linearLayoutHome)
    linearLayoutNetwork = findViewById(R.id.linearLayoutNetwork)
    linearLayoutAddUser = findViewById(R.id.linearLayoutAddUser)

    textViewUsername.text = user.getString("username")

    imageButtonLogout.setOnClickListener {
      user.clear()
      move = Intent(this, LoginActivity::class.java)
      LogoutController(request).invoke(user.getString("token")).call({}, {})
      startActivity(move)
      finish()
    }

    navigation()

    linearLayoutHome.setOnClickListener {
      fragmentPosition = 0
      navigation()
    }

    linearLayoutNetwork.setOnClickListener {
      fragmentPosition = 1
      navigation()
    }

    linearLayoutAddUser.setOnClickListener {
      fragmentPosition = 2
      navigation()
    }
  }

  private fun navigation() {
    when (fragmentPosition) {
      0 -> {
        val fragment = HomeFragment()
        addFragment(fragment)
      }
      1 -> {
        val fragment = NetworkFragment()
        addFragment(fragment)
      }
      2 -> {
        val fragment = AddUserFragment()
        addFragment(fragment)
      }
      else -> {
        val fragment = HomeFragment()
        addFragment(fragment)
      }
    }
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount == 1) {
      finish()
    } else {
      if (fragmentPosition > 0) {
        fragmentPosition = 0
        navigation()
      }
      super.onBackPressed()
    }
  }

  private fun addFragment(fragment: Fragment) {
    val backStateName = fragment.javaClass.simpleName
    val fragmentManager = supportFragmentManager
    val fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0)

    if (!fragmentPopped && fragmentManager.findFragmentByTag(backStateName) == null) {
      val fragmentTransaction = fragmentManager.beginTransaction()
      fragmentTransaction.replace(R.id.contentFragment, fragment, backStateName)
      fragmentTransaction.addToBackStack(backStateName)
      fragmentTransaction.commit()
    }
  }
}