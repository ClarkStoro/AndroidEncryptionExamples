package com.clarkstoro.encryptionexample.presentation.common

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.clarkstoro.encryptionexample.ui.theme.*

@ExperimentalMaterial3Api
@Composable
fun CredentialsTextField(
    value: String,
    label: String,
    isPasswordField: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPasswordField) KeyboardType.Password else KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        singleLine = true,
        shape = RoundedCornerShape(CornerSize(Dimens.size4)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Orange500,
            unfocusedIndicatorColor = Teal200,
            textColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier
    )
}

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

/************************************************* PREVIEWS ***************************************/
@ExperimentalMaterial3Api
@Preview
@Composable
fun EmailTextFieldPreview() {
    CredentialsTextField(
        value = "pippo@prova.it",
        label = stringResource(id = com.clarkstoro.encryptionexample.R.string.email_hint)
    ) {}
}
