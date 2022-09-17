package com.example.kimchimanage.firebase

import android.util.Log
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

  fun signInUser(activity: SignInActivity) {
    mFireStore.collection(Constants.USERS)
      .document(getCurrentId())
      .get()
      .addOnSuccessListener { document ->
        val loggedInUser = document.toObject(User::class.java)!!
        activity.signInSuccess(loggedInUser)
      }.addOnFailureListener {
        e ->
        Log.e(activity.javaClass.simpleName, "Error")
      }
  }

  private fun getCurrentId(): String {
    return FirebaseAuth.getInstance().currentUser!!.uid
  }
}