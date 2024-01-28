package com.clarkstoro.encryptionexample.presentation.asymmetric_encrypt_decrypt

import androidx.lifecycle.ViewModel
import com.clarkstoro.encryptionexample.utils.AsymmetricCryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AsymmetricCryptoViewModel @Inject constructor(
  private val asymmetricCryptoManager: AsymmetricCryptoManager
): ViewModel() {

    val cipherTextResultFlow = MutableStateFlow("")

    // Use the user publicKey for the sake of the example
    // You should put here the publicKey of the receiver of the encrypted message
    val receiverPublicKey = asymmetricCryptoManager.getPublicKey()

    fun getPersonalPublicKey() = asymmetricCryptoManager.getPublicKey()

    fun encryptText(plainText: String, strPublicKey: String = receiverPublicKey) {
        asymmetricCryptoManager.encryptString(plainText, strPublicKey)?.let { encryptedText ->
            Timber.d("Encrypted Text: $encryptedText")
            cipherTextResultFlow.tryEmit(encryptedText)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not encrypt text")
        }
    }

    fun decryptText(textToDecrypt: String) {
        asymmetricCryptoManager.decryptString(textToDecrypt)?.let { plainTextDecryptedBytes ->
            Timber.d("Decrypted Plain Text: $plainTextDecryptedBytes")
            cipherTextResultFlow.tryEmit(plainTextDecryptedBytes)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not decrypt text")
        }
    }
}