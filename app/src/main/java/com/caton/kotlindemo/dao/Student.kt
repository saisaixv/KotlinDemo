package com.caton.kotlindemo.dao

import android.os.Parcel
import android.os.Parcelable

class Student(var name: String) : Parcelable {
    constructor(parcel: Parcel) : this(name = parcel.readString() ?: "") {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(this.name)
    }

    override fun describeContents(): Int {
        return 0;
    }

    fun readFromParcel(dest: Parcel?) {
        this.name = dest!!.readString()?:""
    }

    override fun toString(): String {
        return "Student(name='$name')"
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }



}