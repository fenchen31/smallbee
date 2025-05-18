package com.practice.annotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Route(val path:String)
