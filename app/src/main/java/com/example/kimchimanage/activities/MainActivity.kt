package com.example.kimchimanage.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kimchimanage.R
import com.example.kimchimanage.adapters.BoardItemsAdapter
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.Board
import com.example.kimchimanage.models.User
import com.example.kimchimanage.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
  companion object {
    const val MY_PROFILE_REQUEST_CODE: Int = 11
    const val CREATE_BOARD_REQUEST_CODE: Int = 12
  }

  private lateinit var mUserName: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    setupActionBar()
    nav_view.setNavigationItemSelectedListener(this)

    FireStoreClass().loadUserData(this, true)

    fab_create_board.setOnClickListener {
      val intent = Intent(this,
        CreateBoardActivity::class.java)

      intent.putExtra(Constants.NAME, mUserName)
      startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
    }
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

  fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
    mUserName = user.name

    Glide
      .with(this)
      .load(user.image)
      .centerCrop()
      .placeholder(R.drawable.ic_user_place_holder)
      .into(nav_user_image)

    tv_username.text = user.name

    if(readBoardsList) {
      showProgressDialog(resources.getString(R.string.please_wait))
      FireStoreClass().getBoardList(this)
    }
  }

  override fun onBackPressed() {
    if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      doubleBackToExit()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {
      FireStoreClass().loadUserData(this)
    } else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE) {
      FireStoreClass().getBoardList(this)
    } else {
      Log.e("Cancelled", "Cancelled")

    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.nav_my_profile -> {
        startActivityForResult(
          Intent(this, MyProfileActivity::class.java)
          , MY_PROFILE_REQUEST_CODE
        )
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

  fun populateBoardsListToUi(boardsList: ArrayList<Board>) {
    hideProgressDialog()

    if(boardsList.size > 0) {
      rv_boards_list.visibility = View.VISIBLE
      tv_no_boards_available.visibility = View.GONE

      rv_boards_list.layoutManager = LinearLayoutManager(this)
      rv_boards_list.setHasFixedSize(true)

      val adapter = BoardItemsAdapter(this, boardsList)
      rv_boards_list.adapter = adapter

      adapter.setOnClickListener(object: BoardItemsAdapter.OnClickListener{
        override fun onClick(position: Int, model: Board) {
          val intent = Intent(this@MainActivity, TaskListActivity::class.java)
          intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
          startActivity(intent)
        }
      })
    } else {
      rv_boards_list.visibility = View.GONE
      tv_no_boards_available.visibility = View.VISIBLE
    }
  }
}