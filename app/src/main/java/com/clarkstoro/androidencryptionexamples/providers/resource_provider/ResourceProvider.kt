package com.clarkstoro.androidencryptionexamples.providers.resource_provider

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringRes: Int): String
    fun getString(@StringRes stringRes: Int, vararg formatArgs: Any?): String
    fun getColor(@ColorRes colorRes: Int): Int?
    fun getDrawable(@DrawableRes drawableRes: Int): Drawable?
}