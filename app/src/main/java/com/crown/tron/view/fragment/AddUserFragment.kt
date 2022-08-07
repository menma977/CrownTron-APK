package com.crown.tron.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.crown.tron.R
import com.crown.tron.controller.UserController
import com.crown.tron.http.web.HandleError
import com.crown.tron.modal.Loading
import com.crown.tron.model.User
import com.crown.tron.view.activity.LoginActivity

class AddUserFragment : Fragment() {
  private lateinit var user: User
  private lateinit var request: RequestQueue
  private lateinit var loading: Loading
  private lateinit var move: Intent
  private lateinit var spinnerPackage: Spinner
  private lateinit var name: EditText
  private lateinit var username: EditText
  private lateinit var email: EditText
  private lateinit var password: EditText
  private lateinit var confirm: EditText
  private lateinit var positionLeft: RadioButton
  private lateinit var positionRight: RadioButton
  private lateinit var send: Button

  private var packageIdList = ArrayList<Int>()
  private var packageNameList = ArrayList<String>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_add_user, container, false)

    user = User(requireActivity())
    request = Volley.newRequestQueue(requireActivity())
    loading = Loading(requireActivity())

    spinnerPackage = view.findViewById(R.id.spinnerPackage)
    name = view.findViewById(R.id.editTextName)
    username = view.findViewById(R.id.editTextUsername)
    email = view.findViewById(R.id.editTextEmail)
    password = view.findViewById(R.id.editTextPassword)
    confirm = view.findViewById(R.id.editTextPasswordConfirmation)
    positionLeft = view.findViewById(R.id.radioButtonLeft)
    positionRight = view.findViewById(R.id.radioButtonRight)
    send = view.findViewById(R.id.buttonSend)

    loading.openDialog()
    UserController(request).create(user.getString("token")).call({
      val listPackage = it.getJSONArray("packages")

      for (i in 0 until listPackage.length()) {
        val packages = listPackage.getJSONObject(i)
        val description = packages.getString("name") + " | " + packages.getString("description")
        packageIdList.add(packages.getInt("id"))
        packageNameList.add(description)
      }

      val adapterName = ArrayAdapter(requireActivity(), R.layout.adapter_package_spinner, packageNameList)
      spinnerPackage.adapter = adapterName

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

    send.setOnClickListener {
      loading.openDialog()
      val position = if (positionLeft.isChecked) "left" else "right"
      UserController(request).invoke(
        packageIdList[spinnerPackage.selectedItemPosition],
        name.text.toString(),
        username.text.toString(),
        email.text.toString(),
        password.text.toString(),
        confirm.text.toString(),
        position,
        user.getString("token")
      ).call({
        Toast.makeText(requireActivity(), it.getString("message"), Toast.LENGTH_LONG).show()
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
    }

    return view
  }
}