package com.example.kimchimanage.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.kimchimanage.R
import com.example.kimchimanage.firebase.FireStoreClass
import com.example.kimchimanage.models.Board
import com.example.kimchimanage.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_board.*

import java.io.IOException

class CreateBoardActivity : BaseActivity() {
  private var mSelectedImageFileUri: Uri? = null
  private lateinit var mUsername: String

  private var mBoardImageUrl: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_create_board)

    window.setFlags(
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    setActionBar()

    if(intent.hasExtra(Constants.NAME)) {
      mUsername = intent.getStringExtra(Constants.NAME).toString()
    }

    iv_board_image.setOnClickListener {
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Constants.showImageChooser(this)
      } else {
        ActivityCompat.requestPermissions(
          this,
          arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
          Constants.READ_STORAGE_PERMISSION_CODE
        )
      }
    }

    btn_create.setOnClickListener {
      if(mSelectedImageFileUri != null) {
        uploadBoardImage()
      } else {
        showProgressDialog(resources.getString(R.string.please_wait))
        createBoard()
      }
    }

  }

  private fun setActionBar() {
    setSupportActionBar(toolbar_create_board_activity)

    val actionBar = supportActionBar
    if(actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
      actionBar.title = "Home"

      toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
    }
  }

  private fun createBoard() {
    val assignedUserArrayList: ArrayList<String> = ArrayList()
    assignedUserArrayList.add(getCurrentUserId())

    val board = Board(
      et_board_name.text.toString(),
      mBoardImageUrl,
      mUsername,
      assignedUserArrayList
    )

    FireStoreClass().createBoard(this, board)

  }

  private fun uploadBoardImage() {
    showProgressDialog(resources.getString(R.string.please_wait))

    if (mSelectedImageFileUri != null) {
      val sRef : StorageReference =
        FirebaseStorage.getInstance().reference.child("BOARD_IMAGE" + System.currentTimeMillis() + Constants.getFileExtension(this, mSelectedImageFileUri))

      sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
        taskSnapshot ->
        Log.i(
          "BoardImage URL",
          taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
        )

        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
          uri ->
          Log.i("Downloadable Image URL", uri.toString())
          mBoardImageUrl = uri.toString()
          createBoard()
        }
      }.addOnFailureListener{
        exception ->
        Toast.makeText(
        this,
        exception.message,
        Toast.LENGTH_LONG
        ).show()

        hideProgressDialog()
      }
    }

  }

  fun boardCreatedSuccessfully() {
    hideProgressDialog()
    finish()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
      if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Constants.showImageChooser(this)
      }
    } else {
      Toast.makeText(
        this,
        "Opps you denied persmiisons for storage, You can allow them in settings",
        Toast.LENGTH_LONG
      ).show()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
      mSelectedImageFileUri = data.data
      try {
        Glide
      .with(this)
      .load(mSelectedImageFileUri)
      .centerCrop()
      .placeholder(R.drawable.ic_board_place_holder)
      .into(iv_board_image)
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
}