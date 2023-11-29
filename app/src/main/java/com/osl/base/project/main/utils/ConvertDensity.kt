package com.osl.base.project.main.utils

import android.content.Context

fun dpiToPixels(context: Context, dp: Int): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi / 160f)
}