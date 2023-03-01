package com.fictivestudios.lakoda.apiManager.response

import android.os.Parcel
import android.os.Parcelable

data class HomePostData(
    val comment_count: Int,
    val created_at: String?,
    val description: String?,
    val id: Int,
    val is_post: Int,
    var like_count: Int,
    val share_count: Int,
    val shared_by: SharedBy?,
    val title: String?,
    val type: String?,
    val user: User?,
    val video_image: String?,
    val is_liked: Int,
    val follow_status:String?,
    val is_promoted: Int,
    val original_post_id:Int?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(SharedBy::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(User::class.java.classLoader),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(comment_count)
        parcel.writeString(created_at)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeInt(is_post)
        parcel.writeInt(like_count)
        parcel.writeInt(share_count)
        parcel.writeParcelable(shared_by, flags)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeParcelable(user, flags)
        parcel.writeString(video_image)
        parcel.writeInt(is_liked)
        parcel.writeString(follow_status)
        parcel.writeInt(is_promoted)
        parcel.writeInt(original_post_id!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomePostData> {
        override fun createFromParcel(parcel: Parcel): HomePostData {
            return HomePostData(parcel)
        }

        override fun newArray(size: Int): Array<HomePostData?> {
            return arrayOfNulls(size)
        }
    }
}
