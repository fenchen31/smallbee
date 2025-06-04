package com.practice.compiler

import com.google.auto.service.AutoService
import com.practice.annotation.Route
import java.io.Writer
import java.util.HashMap
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

/**
 * 生成注解代码
 */
@AutoService(Processor::class)
class RouteProcess : AbstractProcessor() {
    var file: Filer? = null

    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        file = p0?.filer
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return HashSet<String>().apply {
            //TODO 1.需要处理哪种注解
            add(Route::class.java.canonicalName)
        }
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        //TODO 2.该注解支持哪个jdk版本，这里是跟随编译器使用的版本，兼容性较好，也可以直接指定版本，但是后续改动的时候也需要考虑同步改动
        return processingEnv.sourceVersion
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        environment: RoundEnvironment?
    ): Boolean {
        //TODO 3.注解逻辑
        //1.获取所有被特定注解处理器（Route）修饰的类
        val elements = environment?.getElementsAnnotatedWith(Route::class.java)
        HashMap<String, String>().apply {
            elements?.forEach {
                val element = it as TypeElement
                val activityName = element.qualifiedName.toString()
                val annotation = element.getAnnotation(Route::class.java).path
                //2.以注解名为key，activity名为value存入hashmap
                put(annotation, activityName)
            }
            //3.生成router文件
            if (size > 0) {
                val className = "RouterUtil"
                var writer: Writer? = null
                try {
                    val moduleName = processingEnv.options["MODULE_NAME"]?.lowercase() ?: "${System.currentTimeMillis()}"
                    //编译为kotlin版本
                    writer = file?.createResource(StandardLocation.SOURCE_OUTPUT, "", "com/practice/router/$moduleName/$className.kt")?.openWriter()
                    writer?.write(StringBuilder().apply {
                        append("package com.practice.router.$moduleName\n")
                        append("import com.practice.core.IRouter\n")
                        append("import com.practice.core.ARouter\n")
                        append("public class ${className}: IRouter {\n")
                        append("    override fun putActivity() {\n")
                        for (it in entries) {
                            append("        ARouter.getInstance().addActivity(\"${it.key}\",${it.value}::class.java)\n")
                        }
                        append("    }\n}\n")
                    }.toString())
                    /*//编译为java版本
                    writer = file?.createSourceFile("com/practice/router/&moduleName/$className")?.openWriter()
                    writer?.write(StringBuilder().apply {
                        append("package com.practice.router.$moduleName\n")
                        append("import com.practice.core.IRouter;\n")
                        append("import com.practice.core.ARouter;\n")
                        append("public class ${className} implements IRouter {\n")
                        append("    @Override\n")
                        append("    public void putActivity() {\n")
                        for (it in entries) {
                            append("        ARouter.getInstance().addActivity(\"${it.key}\",${it.value}.class);\n")
                        }
                        append("    }\n}\n")
                    }.toString())*/
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    writer?.close()
                }
            }
        }
        println("注解代码以执行完成")
        return false
    }
}