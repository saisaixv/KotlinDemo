package com.caton.kotlindemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.caton.kotlindemo.R
import com.caton.kotlindemo.nio.ServerConnect
import kotlinx.android.synthetic.main.activity_nio.*
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.io.RandomAccessFile
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NIOActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nio)
        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setTitle("NIO")
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        btn_start_server.setOnClickListener {
            Thread(Runnable {
//                server()
                ServerConnect.selector()
            }).start()
        }
        btn_start_client1.setOnClickListener {
            Thread(Runnable {
                client("client-1")
            }).start()
        }
        btn_start_client2.setOnClickListener {
            Thread(Runnable {
                client("client-2")
            }).start()
        }
        btn_start_client3.setOnClickListener {
            Thread(Runnable {
                client("client-3")
            }).start()
        }
    }

    companion object {
        var TAG =NIOActivity::class.java.simpleName
    }

    fun client(name:String) {
        val buffer = ByteBuffer.allocate(1024)
        var socketChannel: SocketChannel? = null
        try {
            socketChannel = SocketChannel.open()
            socketChannel.configureBlocking(false)

            socketChannel.connect(InetSocketAddress("127.0.0.1", 8080))
            if (socketChannel.finishConnect()) {
                var i = 0
                while (true) {
                    TimeUnit.SECONDS.sleep(1)
                    var info = "I'm $name $i-th information from client"
                    i++
                    buffer.clear()
                    buffer.put(info.toByteArray())
                    buffer.flip()
                    while (buffer.hasRemaining()) {
                        Log.e(TAG, "write = " + info)
                        socketChannel.write(buffer)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun server() {
        var serverSocket: ServerSocket? = null
        var input: InputStream? = null
        try {
            serverSocket = ServerSocket(8080)
            var recvMsgSize :Int
            var recvBuf = ByteArray(1024)
            while (true) {
                val clntSocket = serverSocket.accept()
                val clientAddress = clntSocket.remoteSocketAddress
                Log.e(TAG, "Handling client at $clientAddress")
                input = clntSocket.getInputStream()

                while (true) {
                    recvMsgSize = input.read(recvBuf)
                    if (recvMsgSize != -1) {
                        var temp = ByteArray(recvMsgSize)
                        System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize)

                        Log.e(TAG, "recv = " + String(temp))

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close()
                }
                if (input != null) {
                    input.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun ReadFileUseByteBuffer(): Unit {
        var aFile:RandomAccessFile?=null
        var fc:FileChannel?=null

        try {
            aFile= RandomAccessFile("src/aaa/txt","rw")
            val fc = aFile.channel
            val timeBegin = System.currentTimeMillis()
            val buff = ByteBuffer.allocate(aFile.length().toInt())
            buff.clear()
            fc.read(buff)
            var timeEnd=System.currentTimeMillis()
            Log.e(TAG,"Read time: "+(timeEnd-timeBegin)+"ms")
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            try {
                if(aFile!=null){
                    aFile.close()
                }
                if(fc!=null){
                    fc.close()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun ReadFileUseMappedByteBuffer(): Unit {
        var aFile:RandomAccessFile?=null
        var fc:FileChannel?=null
        try {
            aFile= RandomAccessFile("src/aaa.txt","rw")
            fc=aFile.channel
            var timeBegin=System.currentTimeMillis()
            var mbb=fc.map(FileChannel.MapMode.READ_ONLY,0,aFile.length())
            var timeEnd=System.currentTimeMillis()
            Log.e(TAG,"Read time: "+(timeEnd-timeBegin)+"ms")
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            try{
                if(aFile!=null){
                    aFile.close()
                }
                if(fc!=null){
                    fc.close()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    //（NIO）使用RandomAccessFile进行操作，
    // 当然也可以通过FileInputStream.getChannel()进行操作
    fun ReadFIleByNIO() {
        var aFile: RandomAccessFile? = null
        try {
            aFile = RandomAccessFile("src/aaa/txt", "rw")
            val fileChannel = aFile.channel
            val buf = ByteBuffer.allocate(1024)
            var bytesRead = fileChannel.read(buf)
            var byteArray = ByteArray(1024)
            Log.e(TAG, "read length = " + bytesRead)
            while (bytesRead != -1) {
                buf.flip()

                var index = 0
                while (buf.hasRemaining()) {
                    byteArray[index] = buf.get()
                    index++
                }
                buf.compact()
                bytesRead = fileChannel.read(buf)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (aFile != null) {
                    aFile.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //（传统IO）使用FIleInputStream读取文件内容
    fun ReadFileByFileInputStream(): Unit {
        var input: InputStream? = null
        try {
            input = BufferedInputStream(FileInputStream("src/aaa.txt"))
            var buf = ByteArray(1024)
            var bytesRead = input.read(buf)
            while (bytesRead != -1) {
                Log.e(TAG, "read = " + String(buf, 0, bytesRead))
                bytesRead = input.read(buf)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (input != null) {
                    input.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
}
