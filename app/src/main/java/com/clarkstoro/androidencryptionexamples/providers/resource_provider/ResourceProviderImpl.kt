package com.clarkstoro.androidencryptionexamples.providers.resource_provider

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(private val context: Context) : ResourceProvider {
    override fun getString(stringRes: Int): String = try {
        context.getString(stringRes)
    } catch (e: NotFoundException) { "" }

    override fun getString(stringRes: Int, vararg formatArgs: Any?): String = try {
        context.getString(stringRes, *formatArgs)
    } catch (e: NotFoundException) { "" }

    override fun getColor(colorRes: Int): Int? = try {
        ContextCompat.getColor(context, colorRes)
    } catch (e: NotFoundException) { null }

    override fun getDrawable(drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableRes)
    }
}