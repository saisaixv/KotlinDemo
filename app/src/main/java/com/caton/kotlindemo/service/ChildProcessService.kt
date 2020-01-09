package com.caton.kotlindemo.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteCallbackList
import android.util.Log
import com.caton.kotlindemo.IMyAidlInterfaceCallback
import com.caton.kotlindemo.dao.Student
import com.caton.kotlindemo.dao.StudentAidlInterface
import java.util.*

class ChildProcessService : Service() {

    companion object {
        var TAG = ChildProcessService.javaClass.name

    }

    val callbacks: RemoteCallbackList<IMyAidlInterfaceCallback> = RemoteCallbackList()
    var callbackCount: Int = 0
    var timer: Timer? = null

    var handler = Handler(Looper.getMainLooper())
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
        //可以在创建匿名对象时候实现抽象函数，
        //因为在 MyBind类中无法使用ChildProcessService中的变量
        //所以在MyBind的匿名对象中实现
        return object : MyBind() {
            override fun registerCallback(cb: IMyAidlInterfaceCallback?) {
                if (cb != null && callbacks.register(cb)) {
                    callbackCount++
                    if (callbackCount != 0 && timer == null) {

                        val task = object : TimerTask() {
                            override fun run() {

                                updateTrafficRate()
                                Thread.sleep(200)
                                updateState()
                            }
                        }
                        timer = Timer(true)
                        timer!!.schedule(task, 1000, 1000)
                    }

                    cb.trafficUpdated(1, 1, 1, 1)
                    cb.stateChanged(1, "name", "msg")
                }
            }

            override fun unregisterCallback(cb: IMyAidlInterfaceCallback?) {
                if (cb != null && callbacks.unregister(cb)) {
                    callbackCount -= 1
                    if (callbackCount == 0 && timer != null) {
                        timer!!.cancel()
                        timer = null
                    }
                }
            }
        }

    }

    fun updateTrafficRate() {
        handler.post {
            if (callbackCount > 0) {
                val n = callbacks.beginBroadcast()
                //从 0 到 n
                for (i in 0 until n) {
                    callbacks.getBroadcastItem(i)
                        .trafficUpdated(2, 2, 2, 2)
                }

                callbacks.finishBroadcast()
            }
        }
    }

    fun updateState() {
        handler.post {
            if (callbackCount > 0) {
                val n = callbacks.beginBroadcast()
                //从 0 到 n
                for (i in 0 until n) {
                    callbacks.getBroadcastItem(i)
                        .stateChanged(2, "name", "msg")
                }

                callbacks.finishBroadcast()
            }
        }
    }


    abstract class MyBind : StudentAidlInterface.Stub() {
//        override fun registerCallback(cb: IMyAidlInterfaceCallback?) {
//
//        }
//
//        override fun unregisterCallback(cb: IMyAidlInterfaceCallback?) {
//
//        }

        override fun getName(stu: Student?): String {
            return stu!!.name
        }

        override fun study(stu: Student?) {
            Log.e(TAG, "我学会了 AIDL的使用")
        }

    }

}