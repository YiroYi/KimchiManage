package com.example.kimchimanage.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.kimchimanage.R
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    setupActionBar()
    nav_view.setNavigationItemSelectedListener(this)

    FireStoreClass().loadUserData(this)
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar_main_activity)
    toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)

    toolbar_main_activity.setNavigationOnClickListener {
      toggleDrawer()
    }
  }

  private fun toggleDrawer() {
    if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      drawer_layout.openDrawer(GravityCompat.START)
    }
  }

  fun updateNavigationUserDetails(user: User) {
    Glide
      .with(this)
      .load(user.image)
      .centerCrop()
      .placeholder(R.drawable.ic_user_place_holder)
      .into(nav_user_image)

    tv_username.text = user.name
  }

  override fun onBackPressed() {
    if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      doubleBackToExit()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.nav_my_profile -> {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
      }

      R.id.nav_sign_out -> {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
        finish()
      }


    }
    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}