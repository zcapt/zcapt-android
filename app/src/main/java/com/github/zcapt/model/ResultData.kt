package com.github.zcapt.model

import android.os.Parcel
import android.os.Parcelable

class ResultData() : Parcelable {
    var status: Int = -1
    var reason: String? = null

    constructor(parcel: Parcel) : this() {
        status = parcel.readInt()
        reason = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status)
        parcel.writeString(reason)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "ResultData(status=$status, reason=$reason)"
    }

    companion object CREATOR : Parcelable.Creator<ResultData> {
        override fun createFromParcel(parcel: Parcel): ResultData {
            return ResultData(parcel)
        }

        override fun newArray(size: Int): Array<ResultData?> {
            return arrayOfNulls(size)
        }
    }
}