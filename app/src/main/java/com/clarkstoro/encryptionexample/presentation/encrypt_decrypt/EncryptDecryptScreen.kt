package com.clarkstoro.encryptionexample.presentation.encrypt_decrypt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.clarkstoro.encryptionexample.presentation.HomeScreenViewModel
import com.clarkstoro.encryptionexample.ui.theme.Dimens
import com.clarkstoro.encryptionexample.ui.theme.Orange500
import com.clarkstoro.encryptionexample.ui.theme.Teal200

@Composable
fun EncryptDecryptScreen(viewModel: HomeScreenViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ENCRYPT DECRYPT",
            modifier = Modifier,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier)
        Text(
            text = "ENCRYPTION",
            modifier = Modifier,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        InputItem(
            value = "",
            label = "Text to encrypt"
        ) {}
        Spacer(modifier = Modifier)
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Encrypt")
        }
        Text(
            text = "Encrypted Text",
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier)
        Spacer(modifier = Modifier)
        Text(
            text = "DEECRYPTION",
            modifier = Modifier,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        InputItem(
            value = "",
            label = "Text to decrypt"
        ) {}
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Decrypt")
        }
        Text(
            text = "Decrypted Text",
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputItem(
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