package com.example.zibro.model

import android.os.Parcel
import android.os.Parcelable

data class Article(
    var id: String = "",
    var classify: String = "",
    var comment: Int = 0,
    var context: String = "",
    var good: Int = 0,
    var time: String = "",
    var title: String = "",
    var username: String = "",
    var view: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(classify)
        parcel.writeInt(comment)
        parcel.writeString(context)
        parcel.writeInt(good)
        parcel.writeString(time)
        parcel.writeString(title)
        parcel.writeString(username)
        parcel.writeInt(view)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}
