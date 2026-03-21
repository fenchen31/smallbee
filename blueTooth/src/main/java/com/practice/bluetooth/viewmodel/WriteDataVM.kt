package com.practice.bluetooth.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class WriteDataVM : ViewModel() {
    val receiveData = ObservableField("")
    val sendData = ObservableField("")
}