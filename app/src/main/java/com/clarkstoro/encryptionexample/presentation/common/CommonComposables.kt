package com.clarkstoro.encryptionexample.presentation.common

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.clarkstoro.encryptionexample.ui.theme.Amber700

@Composable
fun BoilerplateDefaultButton(
    textId: Int,
    enabled: Boolean = true,
    color: Color = Amber700,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) { Text(text = stringResource(id = textId), color = Color.White) }
}