package com.practice.smallbee.binder

class BinderTest {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun write()

    external fun read()
}