package com.clarkstoro.encryptionexample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clarkstoro.encryptionexample.utils.CryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(

) : ViewModel() {

    private val cryptoManager = CryptoManager()

    val cipherTextResultFlow = MutableStateFlow("")


    init {
        encryptText("Hello world!")
    }

    fun encryptText(plainText: String) {
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
            /*
            val cipherText = cryptoManager.encryptToString(plainText.toByteArray())
            Timber.d("MIO TEST - cipherText: $cipherText")

            val plainTextDecryptedBytes = cryptoManager.decryptFromString(cipherText)
            val plainTextDecrypted = String(plainTextDecryptedBytes, StandardCharsets.UTF_8)
            Timber.d("MIO TEST - plain text decrypted: $plainTextDecrypted")
            */


            // Valori di pixel dell'immagine
            val pixelValues = intArrayOf(1, 2, 3, 4)

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



        }
    }

}