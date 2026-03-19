package com.practice.common.base

interface BaseCallback<Data> {
    fun onSuccess(data: Data?)
    fun onFailure(code: Int, message: String?){}
}