package com.example.kimchimanage.firebase

import com.example.kimchimanage.activities.SignUpActivity
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreClass {
  private val mFireStore = FirebaseFirestore.getInstance()

  fun registerUser(activity: SignUpActivity, userInfo: User) {

  }
}