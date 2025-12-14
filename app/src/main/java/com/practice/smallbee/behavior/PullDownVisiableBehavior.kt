package com.practice.smallbee.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
import androidx.core.view.ViewCompat
import com.practice.smallbee.R

class PullDownVisiableBehavior constructor(context: Context, attrs: AttributeSet) :
    Behavior<TextView>(context, attrs) {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: TextView,
        dependency: View
    ): Boolean {
        val view = parent.findViewById<TextView>(R.id.bt_init)
        return view != null && dependency == view
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: TextView,
        dependency: View
    ): Boolean {
        val initTop = dependency.height + dependency.top
        child.y = initTop.toFloat()
        return true
    }
}