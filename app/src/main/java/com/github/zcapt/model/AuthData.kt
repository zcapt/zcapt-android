package com.github.zcapt.model

import android.os.Parcel
import android.os.Parcelable

class AuthData() : Parcelable {
    var status: Int = -1
    var authID: String? = null

    constructor(parcel: Parcel) : this() {
        status = parcel.readInt()
        authID = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status)
        parcel.writeString(authID)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "AuthData(status=$status, authID=$authID)"
    }


    companion object CREATOR : Parcelable.Creator<AuthData> {
        override fun createFromParcel(parcel: Parcel): AuthData {
            return AuthData(parcel)
        }

        override fun newArray(size: Int): Array<AuthData?> {
            return arrayOfNulls(size)
        }
    }
}