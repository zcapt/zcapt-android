package com.github.zcapt.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ZcaptData() : Parcelable {

    @SerializedName("init")
    var initUrl: String? = null
    @SerializedName("large")
    var largeUrl: String? = null
    @SerializedName("small")
    var smallUrl: String? = null
    @SerializedName("veri")
    var veriUrl: String? = null

    constructor(parcel: Parcel) : this() {
        initUrl = parcel.readString()
        largeUrl = parcel.readString()
        smallUrl = parcel.readString()
        veriUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(initUrl)
        parcel.writeString(largeUrl)
        parcel.writeString(smallUrl)
        parcel.writeString(veriUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "ZcaptData(initUrl=$initUrl, largeUrl=$largeUrl, smallUrl=$smallUrl, veriUrl=$veriUrl)"
    }

    companion object CREATOR : Parcelable.Creator<ZcaptData> {
        override fun createFromParcel(parcel: Parcel): ZcaptData {
            return ZcaptData(parcel)
        }

        override fun newArray(size: Int): Array<ZcaptData?> {
            return arrayOfNulls(size)
        }
    }
}