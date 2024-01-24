package com.clarkstoro.encryptionexample.presentation.fingerprint

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.presentation.common.ActionButtons
import com.clarkstoro.encryptionexample.presentation.common.CopyToClipboardButton
import com.clarkstoro.encryptionexample.presentation.common.InputEncryptionDecryption
import com.clarkstoro.encryptionexample.presentation.common.IvModeSelector
import com.clarkstoro.encryptionexample.presentation.common.ReadOnlyInput
import com.clarkstoro.encryptionexample.presentation.common.TitleScreen
import javax.crypto.Cipher

@Composable
fun BiometricScreen(viewModel: BiometricScreenViewModel) {

    val context = LocalContext.current

    val textResult = viewModel.cipherTextResultFlow.collectAsState().value

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

        TitleScreen(title = stringResource(id = R.string.bottom_nav_page3))
        Spacer(modifier = Modifier.height(20.dp))

        IvModeSelector(selectedMode, onModeSelected = {
            selectedMode = it
        })

        InputEncryptionDecryption(
            textToEncryptDecrypt = textToEncryptDecrypt,
            onValueChange = { textToEncryptDecrypt = it }
        )
        Spacer(modifier = Modifier.height(10.dp))

        ActionButtons(
            selectedMode = selectedMode,
            onEncryptAppendMode = {
                val encryptCipher = viewModel.biometricCryptoManager.getEncryptCipher()
                showEncryptBiometricPrompt(
                    context,
                    encryptCipher
                ) {
                    viewModel.encryptAuthAppendMode(textToEncryptDecrypt, encryptCipher)
                }
            },
            onDecryptAppendMode = {
                val decryptCipher = viewModel.biometricCryptoManager.getDecryptCipherFromStringAppendMode(textToEncryptDecrypt)
                decryptCipher?.let {
                    showEncryptBiometricPrompt(
                        context,
                        decryptCipher
                    ) {
                        viewModel.decryptAuthAppendMode(textToEncryptDecrypt, decryptCipher)
                    }
                }
            },
            onEncryptByteArrayMode = {
                val encryptCipher = viewModel.biometricCryptoManager.getEncryptCipher()
                showEncryptBiometricPrompt(
                    context,
                    encryptCipher
                ) {
                    viewModel.encryptAuthArrayMode(textToEncryptDecrypt, encryptCipher)
                }
            },
            onDecryptByteArrayMode = {
                val decryptCipher = viewModel.biometricCryptoManager.getDecryptCipherFromStringByteArrayMode(textToEncryptDecrypt)
                decryptCipher?.let {
                    showEncryptBiometricPrompt(
                        context,
                        decryptCipher
                    ) {
                        viewModel.decryptAuthArrayMode(textToEncryptDecrypt, decryptCipher)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(18.dp))

        ReadOnlyInput(value = textResult)
        Spacer(modifier = Modifier.height(10.dp))

        CopyToClipboardButton(textResult)
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