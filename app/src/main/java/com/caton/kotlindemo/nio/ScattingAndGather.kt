package com.caton.kotlindemo.nio

import java.io.FileOutputStream
import java.lang.Exception
import java.nio.ByteBuffer

class ScattingAndGather {
    companion object {
        fun gather(): Unit {
            var header = ByteBuffer.allocate(10)
            var body = ByteBuffer.allocate(10)
            var b1: ByteArray = "01".toByteArray()
            var b2 = "23".toByteArray()
            header.put(b1)
            body.put(b2)
            var buffs = emptyArray<ByteBuffer>()
            buffs.set(0, header)
            buffs.set(1, body)

            try {
                var os = FileOutputStream("src/scattingAndGather.txt")
                val channel = os.channel
                channel.write(buffs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}