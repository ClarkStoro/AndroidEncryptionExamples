package com.clarkstoro.encryptionexample.presentation.common

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, messageId: Int) {
    Toast.makeText(context, context.getString(messageId), Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}