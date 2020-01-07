package com.caton.kotlindemo.ui.activity

import android.content.Intent
import android.net.LocalServerSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import com.caton.kotlindemo.R
import com.caton.kotlindemo.service.LocalSocketClientService
import kotlinx.android.synthetic.main.activity_local_socket.*
import java.io.InputStream
import java.util.*

class LocalSocketActivity : AppCompatActivity() {

    companion object {
        var SOCKET_NAME = "com.caton.kotlindemo"
        var TAG = "local-Socket"
    }

    lateinit var mServerSocket: LocalServerSocket

    var mHandler = Handler {
        if (it.what == 1) {
            var m = it.obj as String
            tv_msg.text = m
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_socket)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setTitle("LocalSocket")
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        btn_start_client.setOnClickListener(View.OnClickListener {
            startService(Intent(this@LocalSocketActivity, LocalSocketClientService::class.java))
        })
        btn_start_server.setOnClickListener(View.OnClickListener {
            Thread(Runnable {
                kotlin.run {
                    //必须在子线程里接收消息
                    acceptMsg()
                }
            }).start()
        })

        createServerSocket()
    }

    private fun acceptMsg() {
        //accept 是个阻塞方法，这就是必须要在子线程接收消息的原因
        val socket = mServerSocket.accept()
        var input:InputStream
        while (true) {
            var buf = ByteArray(1024)
            input = socket.inputStream
            var len = input.read(buf)
            val key = String(Arrays.copyOfRange(buf, 0, len))
            Log.e(TAG, "Local Server Socket mSocketOutStream = " + key)
            if (key.equals("stop")) {

                break
            }
            val message = mHandler.obtainMessage()
            message.obj = key
            message.sendToTarget()
        }
        input.close()
        socket.close()
        mServerSocket.close()
        Log.e(TAG,"close")
    }

    private fun createServerSocket() {
//        if (mServerSocket == null) {
        mServerSocket = LocalServerSocket(SOCKET_NAME)
//        }
    }
}
