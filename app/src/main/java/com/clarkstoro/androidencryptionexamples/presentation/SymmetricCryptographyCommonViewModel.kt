package com.clarkstoro.androidencryptionexamples.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clarkstoro.domain.usecases.ListenUpdatesEncryptedDataStoredUseCase
import com.clarkstoro.domain.usecases.StoreEncryptedDataUseCase
import com.clarkstoro.androidencryptionexamples.utils.SymmetricCryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class SymmetricCryptographyCommonViewModel @Inject constructor(
    private val symmetricCryptoManager: SymmetricCryptoManager,
    private val storeEncryptedDataUseCase: StoreEncryptedDataUseCase,
    private val listenUpdatesEncryptedDataStoredUseCase: ListenUpdatesEncryptedDataStoredUseCase
) : ViewModel() {

    enum class CryptMode {
        APPEND,
        BYTE_ARRAY
    }

    val cipherTextResultFlow = MutableStateFlow("")
    val currentEncryptedStoredValueFlow = MutableStateFlow("")

    /**
     * MODE = String append
     */

    fun encryptTextAppendingMode(plainText: String) {
        symmetricCryptoManager.encryptStringAppendMode(plainText)?.let { encryptedText ->
            Timber.d("Append Mode - Encrypted Text (iv + cipher text): $encryptedText")
            cipherTextResultFlow.tryEmit(encryptedText)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not encrypt text")
        }
    }

    fun decryptAppendingMode(textToDecrypt: String) {
        symmetricCryptoManager.decryptStringAppendMode(textToDecrypt)?.let { plainTextDecryptedBytes ->
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

    fun encryptTextArrayMode(plainText: String) {
        symmetricCryptoManager.encryptToStringByteArrayMode(plainText.toByteArray())?.let { encryptedText ->
            Timber.d("Byte Array Mode - Encrypted Text (iv + cipher text): $encryptedText")
            cipherTextResultFlow.tryEmit(encryptedText)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not encrypt text")
        }
    }

    fun decryptArrayMode(textToDecrypt: String) {
        symmetricCryptoManager.decryptFromStringByteArrayMode(textToDecrypt)?.let { plainTextDecryptedBytes ->
            val plainTextDecrypted = String(plainTextDecryptedBytes, StandardCharsets.UTF_8)
            Timber.d("Byte Array Mode - Decrypted Plain Text: $plainTextDecrypted")
            cipherTextResultFlow.tryEmit(plainTextDecrypted)
        } ?: run {
            cipherTextResultFlow.tryEmit("Error: could not decrypt text")
        }
    }


    // -------------------------------------------------------------------------

    /**
     *
     * Save/Retrieve on datastore
     *
     */

    fun saveEncryptedData(encryptedData: String) {
        viewModelScope.launch {
            storeEncryptedDataUseCase(encryptedData)
        }
    }

    fun listenEncryptedDataStored() {
        viewModelScope.launch {
            listenUpdatesEncryptedDataStoredUseCase().collectLatest { currentStoredValue ->
                val storedValue = currentStoredValue ?: "No stored value found"
                Timber.d("My encrypted data retrieved: $storedValue")
                currentEncryptedStoredValueFlow.tryEmit(storedValue)
            }
        }
    }
}