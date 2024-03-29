package com.fictivestudios.lakoda.apiManager.response

import android.os.Parcel
import android.os.Parcelable

data class SharedBy(
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SharedBy> {
        override fun createFromParcel(parcel: Parcel): SharedBy {
            return SharedBy(parcel)
        }

        override fun newArray(size: Int): Array<SharedBy?> {
            return arrayOfNulls(size)
        }
    }
}