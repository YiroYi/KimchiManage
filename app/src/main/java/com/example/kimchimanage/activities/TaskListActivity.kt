package com.example.kimchimanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.kimchimanage.R
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.Board
import com.example.kimchimanage.utils.Constants
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_task_list)

     window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    var boardDocumentId = ""
    if(intent.hasExtra(Constants.DOCUMENT_ID)) {
      boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
    }

    showProgressDialog(resources.getString((R.string.please_wait)))
    FireStoreClass().getBoardDetails(this, boardDocumentId)
  }

  private fun setupActionBar(title: String) {
    setSupportActionBar(toolbar_task_list_activity)

    val actionBar = supportActionBar
    if(actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
      actionBar.title = title

      toolbar_task_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
  }

  fun boardDetails(board: Board) {
    hideProgressDialog()
    setupActionBar(board.name)
  }
}