package com.caton.aidlserver

import android.os.Parcel
import android.os.Parcelable

class People() : Parcelable {
    lateinit var name: String

    constructor(parcel: Parcel) : this() {
        this.name=parcel.readString()?:""
    }

    constructor(name: String) : this() {
        this.name = name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.name)
    }

    fun readFromParcel(desc: Parcel) {
        this.name = desc.readString() ?: ""
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "People(name='$name')"
    }

    companion object CREATOR : Parcelable.Creator<People> {
        override fun createFromParcel(parcel: Parcel): People {
            return People(parcel)
        }

        override fun newArray(size: Int): Array<People?> {
            return arrayOfNulls(size)
        }
    }

}