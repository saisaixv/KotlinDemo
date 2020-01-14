package com.caton.kotlindemo.nio

import java.nio.ByteBuffer

class ScattingAndGather {
    companion object{
        fun gather(): Unit {
            var header = ByteBuffer.allocate(10)
            var body=ByteBuffer.allocate(10)
            var b1:ByteArray= "01".toByteArray()
            var b2="23".toByteArray()
            header.put(b1)
            body.put(b2)

        }
    }
}