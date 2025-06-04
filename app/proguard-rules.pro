# 保留 ARouter 类及其所有成员
-keep class com.practice.core.ARouter { *; }

# 保留 ARouter 的 Companion 对象（Kotlin 静态方法支持）
-keep class com.practice.core.ARouter$Companion { *; }

# 保留 IRouter 接口及其实现类（用于路由注册）
-keep interface com.practice.core.IRouter { *; }
-keep class * implements com.practice.core.IRouter { *; }

# 保留所有通过 Class.forName() 调用的类（避免被移除）
-keep class * {
    public <init>(...);
}

# 如果你使用了 APT 生成 RouterUtil 类，也要保留它们
-keep class com.practice.*.RouterUtil* { *; }
