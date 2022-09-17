package com.example.kimchimanage.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.kimchimanage.R
import com.example.kimchimanage.firebase.FireStoreClass
import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    val typeface: Typeface =
      Typeface.createFromAsset(assets, "carbon bl.ttf")
      tv_app_name.typeface = typeface

    Handler(Looper.getMainLooper()).postDelayed({
      var currentUserId = FireStoreClass().getCurrentId()

      if(currentUserId.isNotEmpty()) {
        startActivity(Intent(this, MainActivity::class.java))
      } else {
        startActivity(Intent(this, IntroActivity::class.java))
      }
      finish() }, 2500)
  }
}