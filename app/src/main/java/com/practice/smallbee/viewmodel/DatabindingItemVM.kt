package com.practice.smallbee.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.practice.smallbee.response.UserResponse

class DatabindingItemVM(private val user:UserResponse) : ViewModel() {
    var name = ObservableField<String>(user.name)
    var online = ObservableBoolean(user.online)

    fun onNameChange(newName: String){
        if (newName.isNotEmpty() && !newName.equals(name.get())){
            name.set(newName)
        }
    }

    fun onOnlineChange(){
        online.set(!online.get())
    }

    fun getCurrectUser(): UserResponse{
        return user
    }
}