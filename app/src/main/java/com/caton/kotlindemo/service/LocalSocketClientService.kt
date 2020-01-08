package com.caton.kotlindemo.service

import android.app.Service
import android.content.Intent
import android.net.LocalSocket
import android.net.LocalSocketAddress
import android.os.IBinder
import android.util.Log
import java.io.File
import java.lang.Exception
import java.util.stream.Stream

class LocalSocketClientService : Service(), Runnable {

    companion object {

        //这里必须要跟LocalSocketServer中定义的一样
        var SOCKET_NAME = "com.caton.kotlindemo"
        var PATH = ""
        var TAG = "local-Socket"
    }

    lateinit var mSocket: LocalSocket

    lateinit var address: LocalSocketAddress

    override fun run() {
        //創建localSocket，模拟客户端
        mSocket = LocalSocket()
        //use ABSTRACT
//        address = LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.ABSTRACT)
        //use
        PATH = applicationInfo.dataDir + File.separator + "local_sock"
        address = LocalSocketAddress(PATH, LocalSocketAddress.Namespace.FILESYSTEM)

        while (true) {
            try {
                //连接localSocketServer
                mSocket.connect(address)
                break
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                Thread.sleep(1000)
            }
        }


        var count = 0
        while (count <= 10) {

            val out = mSocket.outputStream
            out.write("current index is $count".toByteArray(Charsets.UTF_8))
            Log.e(TAG, "send msg = " + "current index is $count")
            count++
            Thread.sleep(3000)
        }

        mSocket.outputStream.write("stop".toByteArray(Charsets.UTF_8))

        Log.e(TAG, "finish=====")

    }

    lateinit var mThread: Thread
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        if (mThread == null) {
        mThread = Thread(this@LocalSocketClientService)
//        }
        mThread.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}