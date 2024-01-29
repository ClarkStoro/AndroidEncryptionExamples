package com.clarkstoro.encryptionexample.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.ui.theme.Amber700
import com.clarkstoro.encryptionexample.ui.theme.Dimens


@Composable
fun TitleScreen(title: String) {
    Text(
        text = title,
        modifier = Modifier,
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IvModeSelector(
    selectedMode: CommonViewModel.CryptMode?,
    onModeSelected: (CommonViewModel.CryptMode) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val modesAvailable = CommonViewModel.CryptMode.entries
    var dropdownExpandedState by remember { mutableStateOf(false) }

    androidx.compose.material3.ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = dropdownExpandedState,
        onExpandedChange = { dropdownExpandedState = it }
    ) {
        androidx.compose.material3.OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            value = selectedMode?.name.orEmpty(),
            readOnly = true,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.pick_mode_hint)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = dropdownExpandedState
                )
            }
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = dropdownExpandedState,
            onDismissRequest = {
                dropdownExpandedState = false
            }
        ) {
            modesAvailable.forEach { modeAvailable ->
                DropdownMenuItem(
                    onClick = {
                        onModeSelected.invoke(modeAvailable)
                        dropdownExpandedState = false
                    }
                ) {
                    Text(
                        text = modeAvailable.name,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Composable
fun InputEncryptionDecryption(
    textToEncryptDecrypt: String,
    onValueChange: (String) -> Unit
) {
    CommonInputText(
        isReadOnly = false,
        label = stringResource(id = R.string.encrypt_decrypt_hint),
        value = textToEncryptDecrypt,
        onValueChange = onValueChange
    )
}


@Composable
fun ActionButtons(
    selectedMode: CommonViewModel.CryptMode?,
    onEncryptAppendMode: () -> Unit,
    onDecryptAppendMode: () -> Unit,
    onEncryptByteArrayMode: () -> Unit,
    onDecryptByteArrayMode: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BoilerplateDefaultButton(
            textId = R.string.btn_encrypt
        ) {
            when (selectedMode) {
                CommonViewModel.CryptMode.APPEND -> {
                    onEncryptAppendMode()
                }
                CommonViewModel.CryptMode.BYTE_ARRAY -> {
                    onEncryptByteArrayMode()
                }
                else -> {}
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        BoilerplateDefaultButton(
            textId = R.string.btn_decrypt
        ) {
            when (selectedMode) {
                CommonViewModel.CryptMode.APPEND -> {
                    onDecryptAppendMode()
                }
                CommonViewModel.CryptMode.BYTE_ARRAY -> {
                    onDecryptByteArrayMode()
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ActionButtons(
    onEncrypt: () -> Unit,
    onDecrypt: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BoilerplateDefaultButton(
            textId = R.string.btn_encrypt
        ) {
            onEncrypt()
        }
        Spacer(modifier = Modifier.width(8.dp))
        BoilerplateDefaultButton(
            textId = R.string.btn_decrypt
        ) {
            onDecrypt()
        }
    }
}

@Composable
fun ResultInput(
    value: String
) {
    CommonInputText(
        isReadOnly = true,
        label = stringResource(id = R.string.result_encryption_decryption_hint),
        value = value
    )
}

@Composable
fun CopyToClipboardButton(textToCopy: String) {
    val context = LocalContext.current
    BoilerplateDefaultButton(
        textId = R.string.btn_copy_to_clipboard
    ) {
        copyToClipboard(context, "Result Encryption/Decryption", textToCopy)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonInputText(
    value: String,
    label: String,
    isReadOnly: Boolean = false,
    onValueChange: ((String) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    androidx.compose.material3.OutlinedTextField(
        value = value,
        label = { Text(text = label) },
        onValueChange = {
            onValueChange?.invoke(it)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = VisualTransformation.None,
        trailingIcon = {
            when {
                !isReadOnly && value.isNotBlank() -> {
                    IconButton(
                        onClick = { onValueChange?.invoke("") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(id = com.google.android.material.R.string.clear_text_end_icon_content_description),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                else -> {}
            }
        },
        singleLine = false,
        readOnly = isReadOnly,
        shape = RoundedCornerShape(CornerSize(4.dp)),
        modifier = Modifier.fillMaxWidth()
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