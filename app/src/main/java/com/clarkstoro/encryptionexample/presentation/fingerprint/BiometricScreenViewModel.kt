package com.clarkstoro.encryptionexample.presentation.fingerprint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clarkstoro.encryptionexample.utils.BiometricCryptoManager
import com.clarkstoro.encryptionexample.utils.CryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.inject.Inject

@HiltViewModel
class BiometricScreenViewModel @Inject constructor(
    val biometricCryptoManager: BiometricCryptoManager
) : ViewModel() {

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
            /*cryptoManager.encryptToStringByteArrayMode(plainText.toByteArray())?.let { cipherText ->
                Timber.d("MIO TEST - cipherText: $cipherText")

                cryptoManager.decryptFromStringByteArrayMode(cipherText)?.let { plainTextDecryptedBytes ->
                    val plainTextDecrypted = String(plainTextDecryptedBytes, StandardCharsets.UTF_8)
                    Timber.d("MIO TEST - plain text decrypted: $plainTextDecrypted")
                }
            }*/



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




    // -------------------------------------------------------------------------


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