package com.example.kimchimanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.kimchimanage.R
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.User
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_my_profile)

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    setupActionBar()

    FireStoreClass().loadUserData(this)
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar_my_profile_activity)

    val actionBar = supportActionBar
    if(actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
      actionBar.title = resources.getString(R.string.my_profile)

      toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }
  }

  fun setUseDataInUi(user: User) {
    Glide
      .with(this@MyProfileActivity)
      .load(user.image)
      .centerCrop()
      .placeholder(R.drawable.ic_user_place_holder)
      .into(iv_profile_user_image)

    et_name.setText(user.name)
    et_email.setText(user.email)

    if(user.mobile != 0L) {
      et_mobile.setText(user.mobile.toString())
    }
  }
}