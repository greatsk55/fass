package com.thechange.fass.fass.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by user on 2017. 4. 24..
 */

data class DeleteData(val category:String, val url:String, val urlImage:String, val urlTitle:String, val date:String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<DeleteData> = object : Parcelable.Creator<DeleteData> {
            override fun createFromParcel(source: Parcel): DeleteData = DeleteData(source)
            override fun newArray(size: Int): Array<DeleteData?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(category)
        dest?.writeString(url)
        dest?.writeString(urlImage)
        dest?.writeString(urlTitle)
        dest?.writeString(date)
    }
}
