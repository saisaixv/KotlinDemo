package com.caton.kotlindemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.caton.kotlindemo.R
import com.caton.kotlindemo.nio.ServerConnect
import kotlinx.android.synthetic.main.activity_nio.*
import java.io.*
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.FileChannel
import java.nio.channels.Pipe
import java.nio.channels.SocketChannel
import java.security.spec.ECField
import java.util.concurrent.Callable
import java.util.concurrent.Executors
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
        btn_get_gernal.setOnClickListener {
            useTransferFrom()
        }
        btn_use_pipe.setOnClickListener {
            UsePipe()
        }
        btn_udp_bind.setOnClickListener {
            Thread(Runnable {
                DatagramChannelReceive()
            }).start()

        }
        btn_udp_send.setOnClickListener {
            Thread(Runnable {
                DatagramChannelSend()
            }).start()

        }
    }

    companion object {
        var TAG = NIOActivity::class.java.simpleName
    }

    fun client(name: String) {
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
            var recvMsgSize: Int
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

    //FileChannel的transferFrom()方法可以将数据从源通道传输到FileChannel中
    fun useTransferFrom(): Unit {
        var fromFile: RandomAccessFile? = null
        var toFile: RandomAccessFile? = null
        try {
            fromFile = RandomAccessFile(
                getExternalFilesDir(Environment.MEDIA_MOUNTED)!!.path + "/a.txt",
                "rw"
            )
            var fromChannel = fromFile.channel
            toFile = RandomAccessFile(
                getExternalFilesDir(Environment.MEDIA_MOUNTED)!!.path + "/toFile.txt",
                "rw"
            )
            var toChannel = toFile.channel
            var position = 0L
            var step = 1024L
            var count = fromChannel.size()
            Log.e(TAG, "count = " + count)
            while (position < count) {
                toChannel.transferFrom(fromChannel, position, step)
                position = step + 1
                step += position
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fromFile != null) {
                    fromFile.close()
                }
                if (toFile != null) {
                    toFile.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //transferTo()方法将数据从FileChannel传输到其他的channel中
    fun useTransferTo(): Unit {
        var fromFile: RandomAccessFile? = null
        var toFile: RandomAccessFile? = null
        try {
            val externalFile = File(Environment.getExternalStorageDirectory().absolutePath)
            val listFiles = externalFile.listFiles()
            fromFile = RandomAccessFile(
                getExternalFilesDir(Environment.MEDIA_MOUNTED)!!.path + "/a.txt",
                "rw"
            )
            var fromChannel = fromFile.channel
            toFile = RandomAccessFile(
                getExternalFilesDir(Environment.MEDIA_MOUNTED)!!.path + "/toFile.txt",
                "rw"
            )
            var toChannel = toFile.channel
            var position = 0L
            var count = fromChannel.size()
            Log.e(TAG, "count = " + count)
            fromChannel.transferTo(position, count, toChannel)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fromFile != null) {
                    fromFile.close()
                }
                if (toFile != null) {
                    toFile.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun UsePipe(): Unit {

        var pipe: Pipe? = null
        val exec = Executors.newFixedThreadPool(2)
        try {
            pipe = Pipe.open()
            var pipeTemp = pipe
            exec.submit(Callable {
                //向通道中写数据
                var sinkChannel = pipeTemp.sink()
                while (true) {
                    TimeUnit.SECONDS.sleep(1)
                    var newData = "Pipe Test At Time " + System.currentTimeMillis()
                    val buf = ByteBuffer.allocate(1024)
                    buf.clear()
                    buf.put(newData.toByteArray())
                    buf.flip()
                    while (buf.hasRemaining()) {
                        Log.e(TAG, buf.toString())
                        sinkChannel.write(buf)
                    }
                }
            })

            exec.submit(Callable {
                var sourceChannel = pipeTemp.source()
                while (true) {
                    TimeUnit.SECONDS.sleep(1)
                    var buf = ByteBuffer.allocate(1024)
                    buf.clear()
                    var bytesRead = sourceChannel.read(buf)
                    Log.e(TAG, "bytesRead = " + bytesRead)
                    while (bytesRead > 0) {
                        buf.flip()
                        var b = ByteArray(bytesRead)
                        var i = 0
                        while (buf.hasRemaining()) {
                            b[i] = buf.get()
//                            Log.e(TAG, String.format("%X",b[i]))
                            i++
                        }
                        val s = String(b)
                        Log.e(TAG, "=================||" + s)
                        bytesRead = sourceChannel.read(buf)
                    }
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            exec.shutdown()
        }
    }

    fun DatagramChannelReceive(): Unit {
        var channel: DatagramChannel? = null
        try {
            channel = DatagramChannel.open()
            channel.socket().bind(InetSocketAddress(8888))
            val buf = ByteBuffer.allocate(1024)
            while (true) {
                buf.clear()
                channel.receive(buf)
                buf.flip()
                while (buf.hasRemaining()) {
                    Log.e(TAG, buf.get()?.toChar().toString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (channel != null) {
                    channel.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun DatagramChannelSend(): Unit {

        var channel: DatagramChannel? = null
        try {
            channel = DatagramChannel.open()
            var info = "I'm the Sender!"
            val buf = ByteBuffer.allocate(1024)
            buf.clear()
            buf.put(info.toByteArray())
            buf.flip()

            val bytesSend = channel.send(buf, InetSocketAddress("127.0.0.1", 8888))
            Log.e(TAG, "bytesSend = " + bytesSend)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (channel != null) {
                    channel.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun ReadFileUseByteBuffer(): Unit {
        var aFile: RandomAccessFile? = null
        var fc: FileChannel? = null

        try {
            aFile = RandomAccessFile("src/aaa/txt", "rw")
            val fc = aFile.channel
            val timeBegin = System.currentTimeMillis()
            val buff = ByteBuffer.allocate(aFile.length().toInt())
            buff.clear()
            fc.read(buff)
            var timeEnd = System.currentTimeMillis()
            Log.e(TAG, "Read time: " + (timeEnd - timeBegin) + "ms")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (aFile != null) {
                    aFile.close()
                }
                if (fc != null) {
                    fc.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun ReadFileUseMappedByteBuffer(): Unit {
        var aFile: RandomAccessFile? = null
        var fc: FileChannel? = null
        try {
            aFile = RandomAccessFile("src/aaa.txt", "rw")
            fc = aFile.channel
            var timeBegin = System.currentTimeMillis()
            var mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length())
            var timeEnd = System.currentTimeMillis()
            Log.e(TAG, "Read time: " + (timeEnd - timeBegin) + "ms")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (aFile != null) {
                    aFile.close()
                }
                if (fc != null) {
                    fc.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //（NIO）使用RandomAccessFile进行操作，
    // 当然也可以通过FileInputStream.getChannel()进行操作
    fun ReadFileByNIO() {
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

    //（传统IO）使用FileInputStream读取文件内容
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

    fun GeneralFileWithByteBuffer(fileName: String, size: Long): Unit {
        var fos: FileOutputStream? = null
        var output: FileChannel? = null
        try {
            fos = FileOutputStream(fileName)
            output = fos.channel
            output.write(ByteBuffer.allocate(1), size - 1)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (output != null) {
                    output.close()
                }
                if (fos != null) {
                    fos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun GeneralFileWithRandomAccessFile(fileName: String, size: Long): Unit {
        var start = System.currentTimeMillis()
        var r: RandomAccessFile? = null
        try {
            r = RandomAccessFile(fileName, "rw")
            r.setLength(size)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (r != null) {
                try {
                    r.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        var end = System.currentTimeMillis()
        var use = end - start
        Log.e(TAG, "finish! use time is $use ms.")
    }
}
