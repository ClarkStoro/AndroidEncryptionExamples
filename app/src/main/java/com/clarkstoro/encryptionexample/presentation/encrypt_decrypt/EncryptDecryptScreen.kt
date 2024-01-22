package com.clarkstoro.encryptionexample.presentation.encrypt_decrypt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.presentation.common.BoilerplateDefaultButton
import com.clarkstoro.encryptionexample.presentation.common.copyToClipboard
import com.clarkstoro.encryptionexample.ui.theme.Dimens
import com.clarkstoro.encryptionexample.ui.theme.Orange500
import com.clarkstoro.encryptionexample.ui.theme.Teal200

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EncryptDecryptScreen(viewModel: CommonViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val textResult = viewModel.cipherTextResultFlow.collectAsState().value

    var dropdownExpandedState by remember { mutableStateOf(false) }

    val modesAvailable = CommonViewModel.CryptMode.entries

    var selectedMode: CommonViewModel.CryptMode? by remember {
        mutableStateOf(modesAvailable.firstOrNull())
    }

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.bottom_nav_page1),
            modifier = Modifier,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Mode Selector
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = dropdownExpandedState,
            onExpandedChange = {dropdownExpandedState = !dropdownExpandedState}
        ) {
            androidx.compose.material.OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                value = selectedMode?.name.orEmpty(),
                readOnly = true,
                onValueChange = { },
                label = { androidx.compose.material.Text(stringResource(id = R.string.pick_mode_hint)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownExpandedState
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = dropdownExpandedState,
                onDismissRequest = {
                    dropdownExpandedState = false
                }
            ) {
                modesAvailable.forEach { modeAvailable ->
                    DropdownMenuItem(
                        onClick = {
                            selectedMode = modeAvailable
                            dropdownExpandedState = false
                        }
                    ) {
                        Text(
                            text = modeAvailable.name
                        )
                    }
                }
            }
        }

        // Input Encryption/Decryption
        InputItem(
            modifier = Modifier.fillMaxWidth(),
            value = textToEncryptDecrypt,
            label = stringResource(id = R.string.encrypt_decrypt_hint),
            trailingIcon = {
                when {
                    textToEncryptDecrypt.isNotBlank() -> {
                        IconButton(onClick = { textToEncryptDecrypt = "" }) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = stringResource(id = com.google.android.material.R.string.clear_text_end_icon_content_description))
                        }
                    }
                    else -> {}
                }
            }
        ) {
            textToEncryptDecrypt = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BoilerplateDefaultButton(
                textId = R.string.btn_encrypt
            ) {
                when (selectedMode) {
                    CommonViewModel.CryptMode.APPEND -> {
                        viewModel.encryptTextAppendingMode(textToEncryptDecrypt)
                    }
                    CommonViewModel.CryptMode.BYTE_ARRAY -> {
                        viewModel.encryptTextArrayMode(textToEncryptDecrypt)
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
                        viewModel.decryptAppendingMode(textToEncryptDecrypt)
                    }
                    CommonViewModel.CryptMode.BYTE_ARRAY -> {
                        viewModel.decryptArrayMode(textToEncryptDecrypt)
                    }
                    else -> {}
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))

        // Result Encryption/Decryption
        InputItem(
            modifier = Modifier.fillMaxWidth(),
            value = textResult,
            label = stringResource(id = R.string.result_encryption_decryption_hint),
            isReadOnly = true
        ) {}
        Spacer(modifier = Modifier.height(10.dp))

        // Button Copy to Clipboard
        BoilerplateDefaultButton(
            textId = R.string.btn_copy_to_clipboard
        ) {
            copyToClipboard(context, "Result Encryption/Decryption", textResult)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputItem(
    modifier: Modifier,
    value: String,
    label: String,
    isReadOnly: Boolean = false,
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
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        singleLine = false,
        readOnly = isReadOnly,
        shape = RoundedCornerShape(CornerSize(Dimens.size4)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Orange500,
            unfocusedIndicatorColor = Teal200,
            textColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
    )
}