package com.example.kimchimanage.firebase

import android.app.Activity
import android.util.Log
import com.example.kimchimanage.activities.MainActivity
import com.example.kimchimanage.activities.MyProfileActivity
import com.example.kimchimanage.activities.SignInActivity
import com.example.kimchimanage.activities.SignUpActivity
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

  fun loadUserData(activity: Activity) {
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
            activity.updateNavigationUserDetails(loggedInUser)
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