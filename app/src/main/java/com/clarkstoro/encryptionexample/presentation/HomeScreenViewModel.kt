package com.clarkstoro.encryptionexample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clarkstoro.encryptionexample.utils.CryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cryptoManager: CryptoManager
) : ViewModel() {

    enum class CryptMode {
        APPEND,
        BYTE_ARRAY
    }

    val cipherTextResultFlow = MutableStateFlow("")

    fun encryptTextTest(plainText: String) {
        viewModelScope.launch {
            delay(1000)


            // SILLY SEPARATOR IMPL - OK
            /*
            val cipherText = cryptoManager.encryptString(plainText)
            Timber.d("MIO TEST - cipherText: $cipherText")

            val plainTextDecrypted = cryptoManager.decryptString(cipherText)
            Timber.d("MIO TEST - plain text decrypted: $plainTextDecrypted")

             */

            // BYTE ARRAY IMPL - OK

            val cipherText = cryptoManager.encryptToString(plainText.toByteArray())
            Timber.d("MIO TEST - cipherText: $cipherText")

            val plainTextDecryptedBytes = cryptoManager.decryptFromString(cipherText)
            val plainTextDecrypted = String(plainTextDecryptedBytes, StandardCharsets.UTF_8)
            Timber.d("MIO TEST - plain text decrypted: $plainTextDecrypted")

            // Valori di pixel dell'immagine
            /*val pixelValues = intArrayOf(1, 2, 3, 4)

            // Converte i valori di pixel in un array di byte
            val byteArray = ByteArray(pixelValues.size) { pixelValues[it].toByte() }

            // Stampa l'array di byte
            println("Array di byte: ${byteArray.joinToString(", ")}")


            val outputStream = ByteArrayOutputStream()
            val cipherBytes = cryptoManager.encrypt(byteArray, outputStream)
            Timber.d("MIO TEST - cypher: $cipherBytes")


            val inputStream =  ByteArrayInputStream(outputStream.toByteArray())
            val plainImage = cryptoManager.decrypt(inputStream)

            Timber.d("MIO TEST - plain image: ${plainImage.joinToString(", ")}")
*/


        }
    }

    /**
     * MODE = String append
     */

    fun encryptTextAppendingMode(plainText: String) {
        val cipherText = cryptoManager.encryptStringSillyMode(plainText)
        Timber.d("MIO TEST - cipherText: $cipherText")
        cipherTextResultFlow.tryEmit(cipherText)
    }

    fun decryptAppendingMode(textToDecrypt: String) {
        val plainTextDecryptedBytes = cryptoManager.decryptStringSillyMode(textToDecrypt)
        Timber.d("MIO TEST - plain text decrypted: $plainTextDecryptedBytes")

        cipherTextResultFlow.tryEmit(plainTextDecryptedBytes)
    }

    // -------------------------------------------------------------------------

    /**
     * MODE = ByteArray
     */

    fun encryptTextArrayMode(plainText: String) {
        val cipherText = cryptoManager.encryptToString(plainText.toByteArray())
        Timber.d("MIO TEST - cipherText: $cipherText")
        cipherTextResultFlow.tryEmit(cipherText)
    }

    fun decryptArrayMode(textToDecrypt: String) {
        val plainTextDecryptedBytes = cryptoManager.decryptFromString(textToDecrypt)
        val plainTextDecrypted = String(plainTextDecryptedBytes, StandardCharsets.UTF_8)
        Timber.d("MIO TEST - plain text decrypted: $plainTextDecrypted")

        cipherTextResultFlow.tryEmit(plainTextDecrypted)
    }

}