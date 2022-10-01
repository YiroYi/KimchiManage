package com.example.kimchimanage.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.kimchimanage.activities.*
import com.example.kimchimanage.models.Board
import com.example.kimchimanage.models.User
import com.example.kimchimanage.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FireStoreClass {
  private val mFireStore = FirebaseFirestore.getInstance()

  fun registerUser(activity: SignUpActivity, userInfo: User) {
    mFireStore.collection(Constants.USERS)
      .document(getCurrentId())
      .set(userInfo, SetOptions.merge())
      .addOnSuccessListener {
        activity.userRegisteredSuccess()
      }.addOnFailureListener {
        e ->
        Log.e(activity.javaClass.simpleName, "Error")
      }
  }

  fun createBoard(activity: CreateBoardActivity, board: Board) {
    mFireStore.collection((Constants.BOARDS))
      .document()
      .set(board, SetOptions.merge())
      .addOnSuccessListener {
        Log.e(activity.javaClass.simpleName, "Board created successfully")
        Toast.makeText(
          activity,
          "Board Created successfully",
          Toast.LENGTH_SHORT
        ).show()

        activity.boardCreatedSuccessfully()
      }.addOnFailureListener{
        exception ->
        activity.hideProgressDialog()
        Log.e(
          activity.javaClass.simpleName,
          "Error",
          exception
        )
      }
  }

  fun getBoardList(activity: MainActivity) {
    mFireStore.collection(Constants.BOARDS).whereArrayContains(Constants.ASSIGNED_TO, getCurrentId())
      .get()
      .addOnSuccessListener {
        document ->
        Log.i(activity.javaClass.simpleName, document.documents.toString())
        val boardList: ArrayList<Board> = ArrayList()
        for(i in document.documents) {
          val board = i.toObject((Board::class.java))!!
          board.documentId = i.id
          boardList.add(board)
        }

        activity.populateBoardsListToUi(boardList)
      }.addOnFailureListener{
        e ->
        activity.hideProgressDialog()
        Log.e(activity.javaClass.simpleName, "Error while creating board list", e)
      }
  }

  fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
    mFireStore.collection(Constants.USERS)
      .document(getCurrentId())
      .update(userHashMap)
      .addOnSuccessListener {
        Log.i(activity.javaClass.simpleName, "Profile Updated")
        Toast.makeText(
        activity,
        "Data Updated",
        Toast.LENGTH_LONG
        ).show()
        activity.profileUpdateSuccess()
      }.addOnFailureListener {
        e ->
        activity.hideProgressDialog()
        Log.i(
          activity.javaClass.simpleName,
          "Error while updating",
        e)

        Toast.makeText(
        activity,
        "Something went wrong",
        Toast.LENGTH_LONG
        ).show()
      }
  }

  fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {
    mFireStore.collection(Constants.USERS)
      .document(getCurrentId())
      .get()
      .addOnSuccessListener { document ->
        val loggedInUser = document.toObject(User::class.java)!!

        when(activity) {
          is SignInActivity -> {
            activity.signInSuccess(loggedInUser)
          }

          is MainActivity -> {
            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
          }

          is MyProfileActivity -> {
            activity.setUseDataInUi(loggedInUser)
          }
        }


      }.addOnFailureListener { e ->
        when(activity) {
          is SignInActivity -> {
            activity.hideProgressDialog()
          }

          is MainActivity -> {
            activity.hideProgressDialog()
          }
        }
        Log.e(activity.javaClass.simpleName, "Error")
      }
  }

  fun getCurrentId(): String {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var currentUserId = ""

    if(currentUser != null) {
      currentUserId = currentUser.uid
    }

    return currentUserId
  }
}