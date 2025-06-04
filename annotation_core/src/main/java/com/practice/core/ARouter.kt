package com.practice.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import dalvik.system.DexFile
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class ARouter private constructor() {
    private object Holder{
        val instance = ARouter()
    }
    companion object{
        @JvmStatic
        fun getInstance():ARouter = Holder.instance
    }
    lateinit var context:Context
    lateinit var map: HashMap<String, Class<*>>
    fun addActivity(name: String, clazz: Class<*>){
        if(name.isNotBlank() && !map.containsKey(name)){
            map[name] = clazz
        }
    }

    fun jumpActivity(path:String, bundle: Bundle?){
        try {
            val intent = Intent(context, map[path])
            bundle?.let {
                intent.putExtras(bundle)
            }
            when(context){
                is Activity -> (context as Activity).startActivity(intent)
                else ->{
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun init(context: Context, packageName: String){
        map = HashMap()
        this.context = context
        //TODO 1.获取packageName下所有的类
        val classNames = getCLassName(packageName)
        //TODO 2.生成路由表
        classNames.forEach { className ->
            //获取所有的类名，然后判断这个类是否实现了IRouter，是就直接强转并调用它的putActivity()
            val utilClass = Class.forName(className)
            if (IRouter::class.java.isAssignableFrom(utilClass)){
                val iRouter = utilClass.getDeclaredConstructor().newInstance() as IRouter
                iRouter.putActivity()
            }
        }
    }

    fun getCLassName(packageName: String): ArrayList<String>{
        val list = ArrayList<String>()
        try {
            val dexFile = DexFile(context.packageCodePath)
            val entries = dexFile.entries()
            while (entries.hasMoreElements()){
                val className = entries.nextElement()
                if (className.contains(packageName)){
                     list.add(className)
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        return list
    }

    fun get(packageName: String): ArrayList<String>{
        val list = ArrayList<String>()
        try {
            val pathListField = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList")
            pathListField.isAccessible = true
            val pathList = pathListField.get(context.classLoader)

            val dexElementsField = pathList.javaClass.getDeclaredField("dexElements")
            dexElementsField.isAccessible = true
            val dexElements = dexElementsField.get(pathList) as Array<*>

            dexElements.forEach { element ->
                if (element != null) {
                    val dexFileField = element.javaClass.getDeclaredField("dexFile")
                    dexFileField.isAccessible = true
                    val dexFile = dexFileField.get(element) as DexFile

                    val entries = dexFile.entries()
                    while (entries.hasMoreElements()) {
                        val className = entries.nextElement()
                        if (className.contains(packageName)) {
                            list.add(className)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}