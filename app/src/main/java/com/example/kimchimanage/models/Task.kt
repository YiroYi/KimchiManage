package com.example.kimchimanage.models

import android.os.Parcel
import android.os.Parcelable
import com.projemanag.model.Card

data class Task (
  var title: String = "",
  var createdBy: String = "",
  val cards: ArrayList<Card> = ArrayList()
  ): Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString()!!,
    parcel.readString()!!,
    parcel.createTypedArrayList(Card.CREATOR)!!
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(title)
    writeString(createdBy)
    writeTypedList(cards)
  }

  companion object CREATOR : Parcelable.Creator<Task> {
    override fun createFromParcel(parcel: Parcel): Task {
      return Task(parcel)
    }

    override fun newArray(size: Int): Array<Task?> {
      return arrayOfNulls(size)
    }
  }
}
