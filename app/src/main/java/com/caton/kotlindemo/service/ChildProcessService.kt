package com.caton.kotlindemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.caton.kotlindemo.dao.Student
import com.caton.kotlindemo.dao.StudentAidlInterface

class ChildProcessService : Service() {

    companion object {
        var TAG = ChildProcessService.javaClass.name
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate=======")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand=======")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy=======")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind=======")
        return MyBind()
    }

    class MyBind: StudentAidlInterface.Stub() {
        override fun getName(stu: Student?): String {
            return stu!!.name
        }

        override fun study(stu: Student?) {
            Log.e(TAG,"我学会了 AIDL的使用")
        }

    }

}