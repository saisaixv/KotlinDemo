package com.caton.aidlserver

import android.app.Service
import android.content.Intent
import android.nfc.Tag
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    var TAG="MyService"
    override fun onCreate() {
        super.onCreate()
        Log.e(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG,"onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG,"onBind")
        return MyBind()
    }

    class MyBind : IMyAidlInterface.Stub() {
        override fun addOperation(a: Int, b: Int): Int {
            return a+b
        }

        // in 只允许客户端传递数据到服务端
        override fun getPeopleInfo1(people: People?): String {
            Log.e("MyBind","getPeopleInfo1 = "+people.toString())
            return people.toString()
        }

        override fun getPeopleInfo2(people: People?): String {
            Log.e("MyBind","getPeopleInfo2 = "+people.toString())
            return "getPeopleInfo2"
        }

        override fun getPeopleInfo3(people: People?): String {
            Log.e("MyBind","getPeopleInfo3 = "+people.toString())
            return people.toString()
        }

    }
}