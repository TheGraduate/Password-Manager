package com.example.passwordmanager.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Website(
    val id: Long,
    val name: String,
    val login: String,
    val password: String,
    val dateOfAdding: String,
    val description: String,
    val url: String,
    //val iconURL: String = url + "/favicon.ico"
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(login)
        parcel.writeString(password)
        parcel.writeString(dateOfAdding)
        parcel.writeString(description)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Website> {
        override fun createFromParcel(parcel: Parcel): Website {
            return Website(parcel)
        }

        override fun newArray(size: Int): Array<Website?> {
            return arrayOfNulls(size)
        }
    }
}