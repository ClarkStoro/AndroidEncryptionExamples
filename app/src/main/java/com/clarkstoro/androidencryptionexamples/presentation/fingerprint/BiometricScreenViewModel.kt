package com.clarkstoro.androidencryptionexamples.presentation.fingerprint

import androidx.lifecycle.ViewModel
import com.clarkstoro.androidencryptionexamples.utils.BiometricCryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.inject.Inject

@HiltViewModel
class BiometricScreenViewModel @Inject constructor(
    val biometricCryptoManager: BiometricCryptoManager
) : ViewModel() {

    val cipherTextResultFlow = MutableStateFlow("")

    /**
     * MODE = String append
     */

    fun encryptAuthAppendMode(plainText: String, cipher: Cipher) {
        biometricCryptoManager.encryptStringAppendMode(cipher, plainText)?.let { encryptedText ->
            Timber.d("Append Mode - Encrypted Text (iv + cipher text): $encryptedText")
            cipherTextResultFlow.tryEmit(encryptedText)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not encrypt text")
        }
    }

    fun decryptAuthAppendMode(plainText: String, cipher: Cipher) {
        biometricCryptoManager.decryptStringAppendMode(cipher, plainText)?.let { plainTextDecryptedBytes ->
            Timber.d("Append Mode - Decrypted Plain Text: $plainTextDecryptedBytes")
            cipherTextResultFlow.tryEmit(plainTextDecryptedBytes)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not decrypt text")
        }
    }


    // -------------------------------------------------------------------------

    /**
     * MODE = ByteArray
     */

    fun encryptAuthArrayMode(plainText: String, cipher: Cipher) {
        biometricCryptoManager.encryptToStringByteArrayMode(cipher, plainText.toByteArray())?.let { encryptedText ->
            Timber.d("Byte Array Mode - Encrypted Text (iv + cipher text): $encryptedText")
            cipherTextResultFlow.tryEmit(encryptedText)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not encrypt text")
        }
    }

    fun decryptAuthArrayMode(textToDecrypt: String, cipher: Cipher) {
        biometricCryptoManager.decryptFromStringByteArrayMode(cipher, textToDecrypt)?.let { plainTextDecryptedBytes ->
            val plainTextDecrypted = String(plainTextDecryptedBytes, StandardCharsets.UTF_8)
            Timber.d("Byte Array Mode - Decrypted Plain Text: $plainTextDecrypted")
            cipherTextResultFlow.tryEmit(plainTextDecrypted)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not decrypt text")
        }
    }
}