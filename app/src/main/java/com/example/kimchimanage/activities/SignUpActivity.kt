package com.example.kimchimanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.kimchimanage.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_email

class SignUpActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    setupActionBar()
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar_sign_up_activity)

    val actionBar = supportActionBar
    if(actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)

      toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }

      btn_sign_up.setOnClickListener {
        registerUser()
      }
    }
  }

  private fun registerUser() {
    val name: String = et_name.text.toString().trim() { it <= ' '}
    val email: String = et_email.text.toString().trim() { it <= ' '}
    val password: String = et_password_sign_up.text.toString().trim() { it <= ' '}

    if(validateform(name, email, password)) {
      Toast.makeText(
        this@SignUpActivity,
        "Now we can Register a new User",
        Toast.LENGTH_LONG
      ).show()
    }
  }

  private fun validateform(
    name: String,
    email: String,
    password: String): Boolean {
    return when {
      TextUtils.isEmpty(name) -> {
        showErrorSnackBar("Please enter a name")
        false
      }

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