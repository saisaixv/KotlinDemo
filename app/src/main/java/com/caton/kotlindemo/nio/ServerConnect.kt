package com.caton.kotlindemo.nio

import android.util.Log
import java.lang.Exception
import java.lang.Thread.sleep
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.security.spec.ECField

class ServerConnect {
    companion object {
        val BUF_SIZE = 1024
        val PORT = 8080
        val TIMEOUT: Long = 3000

        val TAG = ServerConnect::class.java.simpleName

        fun handleAccept(key: SelectionKey): Unit {
            var ssChannel: ServerSocketChannel = key.channel() as ServerSocketChannel
            val sc = ssChannel.accept()
            sc.configureBlocking(false)

            sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE))
        }

        fun handleRead(key: SelectionKey): Unit {
            var sc: SocketChannel = key.channel() as SocketChannel
            var buf: ByteBuffer = key.attachment() as ByteBuffer
            var bytesRead = sc.read(buf)
            while (bytesRead > 0) {
                buf.flip()
                var temp = ByteArray(bytesRead)
                var index = 0
                while (buf.hasRemaining()) {
                    temp[index] = buf.get()
                    index++
                }
                Log.e(TAG, "read = " + String(temp, 0, bytesRead))
                buf.clear()
                bytesRead = sc.read(buf)
            }
            if (bytesRead == -1) {
                sc.close()
            }
        }

        fun handleWrite(key: SelectionKey): Unit {
            var buf: ByteBuffer = key.attachment() as ByteBuffer
            buf.flip()
            var sc: SocketChannel = key.channel() as SocketChannel
            while (buf.hasRemaining()) {
                sc.write(buf)
            }
            buf.compact()
        }

        fun selector() {
            var selector: Selector? = null
            var ssc: ServerSocketChannel? = null

            var buf = ByteArray(1024)
            try {
                selector = Selector.open()
                ssc = ServerSocketChannel.open()
                ssc.socket().bind(InetSocketAddress(PORT))
                ssc.configureBlocking(false)
                ssc.register(selector, SelectionKey.OP_ACCEPT)
//                val selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT)
                while (true) {
                    if (selector.select(TIMEOUT) == 0) {
                        Log.e(TAG, "==")
                        continue
                    }
                    val iter = selector.selectedKeys().iterator()

                    while (iter.hasNext()) {
                        val key = iter.next()
                        if (key.isAcceptable) {
                            handleAccept(key)
                        }
                        if (key.isReadable) {
                            handleRead(key)
                        }
                        if (key.isWritable && key.isValid) {
                            handleWrite(key)
                        }

                        if (key.isConnectable) {
                            Log.e(TAG, "isConnectable = true")
                        }
                        iter.remove()
                }

//                    while (selectionKey.isAcceptable) {
//
//                        handleAccept(selectionKey)
//                        if (selectionKey.isReadable) {
//                            handleRead(selectionKey)
//                        }
//                        if (selectionKey.isWritable && selectionKey.isValid) {
//                            handleWrite(selectionKey)
//                        }
//
//                        if (selectionKey.isConnectable) {
//                            Log.e(TAG, "isConnectable = true")
//                        }
//                    }

//                    val accept = ssc.accept()
//                    if (accept != null) {
//                        //每个accept都要起一个线程去处理
//                        val tempAccept=accept
//                        Thread(Runnable {
//                            while (true) {
//                                var len = tempAccept.socket().getInputStream().read(buf)
//                                if (len > 0) {
//                                    var temp = ByteArray(len)
//                                    System.arraycopy(buf, 0, temp, 0, len)
//                                    Log.e(TAG, "recv = " + String(temp))
//                                }
//                            }
//                        }).start()
//
//                    } else {
//                        sleep(1000)
////                        Log.e(TAG, "accept is null")
//                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (selector != null) {
                        selector.close()
                    }
                    if (ssc != null) {
                        ssc.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }


    }


}