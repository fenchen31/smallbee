package com.test.smallbee.util

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

object SharePreferencesUtil {
    open val FILE_NAME = "smallbee"
    lateinit var preferences: SharedPreferences
    lateinit var editor: Editor
    fun init(context: Context, fileName: String){
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun put(key:String, value: Any){
        when(value){
            value as Boolean -> editor.putBoolean(key, value).apply()
            value as Int -> editor.putInt(key, value).apply()
            value as Float -> editor.putFloat(key, value).apply()
            value as Long -> editor.putLong(key, value).apply()
            value as String -> editor.putString(key, value).apply()
            else -> throw IllegalArgumentException("can not put $value to sharepreference, it's type is not support")
        }
    }

    fun getString(key: String, defaultValue: String = ""): String{
        return preferences.getString(key, defaultValue).toString()
    }

    fun getBoolean(key:String, defaultValue: Boolean = false): Boolean{
        return preferences.getBoolean(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int?): Int{
        return preferences.getInt(key, defaultValue ?: -1)
    }
}