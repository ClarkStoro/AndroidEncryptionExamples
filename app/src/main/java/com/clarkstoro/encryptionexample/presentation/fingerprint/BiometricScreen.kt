package com.clarkstoro.encryptionexample.presentation.fingerprint

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.presentation.common.BoilerplateDefaultButton
import com.clarkstoro.encryptionexample.presentation.common.copyToClipboard
import javax.crypto.Cipher

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BiometricScreen(viewModel: BiometricScreenViewModel) {

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


    // ---------------------------------------------------------------------------------------

    val biometricManager = remember { BiometricManager.from(context) }

    // ------------------------------------------------------------



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.bottom_nav_page3),
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
        com.clarkstoro.encryptionexample.presentation.encrypt_decrypt.InputItem(
            modifier = Modifier.fillMaxWidth(),
            value = textToEncryptDecrypt,
            label = stringResource(id = R.string.encrypt_decrypt_hint),
            trailingIcon = {
                when {
                    textToEncryptDecrypt.isNotBlank() -> {
                        IconButton(onClick = { textToEncryptDecrypt = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(id = com.google.android.material.R.string.clear_text_end_icon_content_description)
                            )
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
                val encryptCipher = viewModel.biometricCryptoManager.getEncryptCipher()
                showEncryptBiometricPrompt(
                    context,
                    encryptCipher
                ) {
                    when (selectedMode) {
                        CommonViewModel.CryptMode.APPEND -> {
                            viewModel.encryptAuthAppendMode(textToEncryptDecrypt, encryptCipher)
                        }
                        CommonViewModel.CryptMode.BYTE_ARRAY -> {
                            viewModel.encryptAuthArrayMode(textToEncryptDecrypt, encryptCipher)
                        }
                        else -> {}
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            BoilerplateDefaultButton(
                textId = R.string.btn_decrypt
            ) {


                val decryptCipher = when(selectedMode) {
                    CommonViewModel.CryptMode.APPEND -> {
                        viewModel.biometricCryptoManager.getDecryptCipherFromStringAppendMode(textToEncryptDecrypt)
                    }
                    CommonViewModel.CryptMode.BYTE_ARRAY -> {
                        viewModel.biometricCryptoManager.getDecryptCipherFromStringByteArrayMode(textToEncryptDecrypt)
                    }
                    else -> null
                }

                decryptCipher?.let {
                    showEncryptBiometricPrompt(
                        context,
                        decryptCipher
                    ) {
                        when (selectedMode) {
                            CommonViewModel.CryptMode.APPEND -> {
                                viewModel.decryptAuthAppendMode(textToEncryptDecrypt, decryptCipher)
                            }
                            CommonViewModel.CryptMode.BYTE_ARRAY -> {
                                viewModel.decryptAuthArrayMode(textToEncryptDecrypt, decryptCipher)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))

        // Result Encryption/Decryption
        com.clarkstoro.encryptionexample.presentation.encrypt_decrypt.InputItem(
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

val promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    .setTitle("Biometric Authentication")
    .setSubtitle("Log in using your biometric credential")
    .build()


private fun showEncryptBiometricPrompt(
    context: Context,
    cipher: Cipher,
    onFailed: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    onSuccess: () -> Unit
) {

    val biometricPromptEncrypt = BiometricPrompt(
        context as FragmentActivity,
        ContextCompat.getMainExecutor(context),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError?.invoke()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed?.invoke()
            }
        }
    )

    biometricPromptEncrypt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
}