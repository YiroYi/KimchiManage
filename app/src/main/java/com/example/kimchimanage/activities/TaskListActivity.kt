package com.example.kimchimanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kimchimanage.R
import com.example.kimchimanage.adapters.TaskListItemsAdapter
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.Board
import com.example.kimchimanage.models.Task
import com.example.kimchimanage.utils.Constants
import com.projemanag.model.Card
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {
  private lateinit var mBoardDetails : Board
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

  private fun setupActionBar() {
    setSupportActionBar(toolbar_task_list_activity)

    val actionBar = supportActionBar
    if(actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
      actionBar.title = mBoardDetails.name

      toolbar_task_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
  }

  fun boardDetails(board: Board) {
    mBoardDetails = board

    hideProgressDialog()
    setupActionBar()

    val addTaskList = Task(resources.getString(R.string.add_list))
    board.taskList.add(addTaskList)

    rv_task_list.layoutManager = LinearLayoutManager(
      this,
      LinearLayoutManager.HORIZONTAL,
      false
    )

    rv_task_list.setHasFixedSize(true)

    val adapter = TaskListItemsAdapter(this, board.taskList)
    rv_task_list.adapter = adapter
  }

  fun addUpdateTaskListSuccess() {
    hideProgressDialog()
    showProgressDialog(resources.getString(R.string.please_wait))
    FireStoreClass().getBoardDetails(this, mBoardDetails.documentId)
  }

  fun createTaskList(taskListName: String) {
    val task = Task(taskListName, FireStoreClass().getCurrentId())
    mBoardDetails.taskList.add(0 , task)
    mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1 )

    showProgressDialog(resources.getString(R.string.please_wait))
    FireStoreClass().addUpdateTaskList(this, mBoardDetails)
  }

  fun updateTaskList(position: Int, listName: String, model: Task) {
    val task = Task(listName, model.createdBy)
    mBoardDetails.taskList[position] = task
    mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1)

    showProgressDialog(resources.getString(R.string.please_wait))
    FireStoreClass().addUpdateTaskList(this, mBoardDetails)

  }

  fun deleteTaskList(position: Int) {
    mBoardDetails.taskList.removeAt(position)

    mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1)

    showProgressDialog(resources.getString(R.string.please_wait))
    FireStoreClass().addUpdateTaskList(this, mBoardDetails)
  }

  fun addCardToTaskList(position: Int, cardName: String) {
    mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1)

    val cardAssignedUserList: ArrayList<String> = ArrayList()
    cardAssignedUserList.add(FireStoreClass().getCurrentId())

    val card = Card(cardName, FireStoreClass().getCurrentId(), cardAssignedUserList)

    val cardsList = mBoardDetails.taskList[position].cards
    cardsList.add(card)

    val task = Task(
      mBoardDetails.taskList[position].title,
      mBoardDetails.taskList[position].createdBy,
      cardsList
    )

    mBoardDetails.taskList[position] = task

    showProgressDialog(resources.getString(R.string.please_wait))
    FireStoreClass().addUpdateTaskList(this, mBoardDetails)
  }
}