package com.practice.common.service

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data :T)