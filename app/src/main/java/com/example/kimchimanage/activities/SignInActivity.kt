package com.example.kimchimanage.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.kimchimanage.R
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_in)

    auth = FirebaseAuth.getInstance()

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    btn_sign_in.setOnClickListener {
      signInRegisteredUser()
    }

    setActionBar()
  }

  fun signInSuccess(user: User) {
    hideProgressDialog()
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }

  private fun setActionBar() {
    setSupportActionBar(toolbar_sign_in_activity)

    val actionBar = supportActionBar
    if(actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)

      toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }
  }

  private fun signInRegisteredUser() {
    val email: String = et_email_sign_in.text.toString().trim()
    val password: String = et_password.text.toString().trim()

    if (validateForm(email, password)) {
      showProgressDialog(resources.getString(R.string.please_wait))

      auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            hideProgressDialog()
            if (task.isSuccessful) {
                FireStoreClass().loadUserData(this)
            } else {
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }
        }

    }
  }

  private fun validateForm(
    email: String,
    password: String): Boolean {

    return when {
      TextUtils.isEmpty(email) -> {
        showErrorSnackBar("Please enter a email")
        false
      }

      TextUtils.isEmpty(password) -> {
        showErrorSnackBar("Please enter a password")
        false
      } else -> {
        true
      }
    }
  }
}