package com.fictivestudios.lakoda.apiManager.response

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: Int,
    val image: String,
    val name: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }



    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel!!.writeInt(id)
            parcel.writeString(image)
            parcel.writeString(name)

    }


}