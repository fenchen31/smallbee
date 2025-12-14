package com.practice.common.util

import android.content.res.Resources
import android.util.TypedValue

val Float.px
    get() = this / Resources.getSystem().displayMetrics.density

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
val Int.px
    get() = this.toFloat().px
val Int.dp
    get() = this.toFloat().dp