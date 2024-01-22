package com.clarkstoro.encryptionexample.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


class CryptoManager {


    companion object {

        private const val KEYSTORE = "AndroidKeyStore"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        private const val GCM_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val GCM_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val GCM_TAG_LENGHT = 128

        private const val CBC_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val CBC_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7

        private const val CURRENT_BLOCK_MODE = GCM_BLOCK_MODE
        private const val CURRENT_PADDING = GCM_PADDING

        private const val TRANSFORMATION = "$ALGORITHM/$CURRENT_BLOCK_MODE/$CURRENT_PADDING"

        private const val ALIAS_KEY = "MY_ALIAS_KEY"
        private const val KEY_SIZE = 256

        private const val SILLY_SEPARATOR = "|||"
    }


    private val keystore = KeyStore.getInstance(KEYSTORE).apply {
        load(null)
    }

    private fun getEncryptCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            when(CURRENT_BLOCK_MODE) {
                GCM_BLOCK_MODE -> {
                    val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGHT, iv)
                    init(Cipher.DECRYPT_MODE, getKey(), gcmParameterSpec)
                }
                CBC_BLOCK_MODE -> {
                    init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
                }
                else -> {
                    throw Exception()
                }
            }
        }
    }

    private fun getKey(): SecretKey {
        val entry = keystore.getEntry(ALIAS_KEY, null) as? SecretKeyEntry
        return entry?.secretKey ?: generateKey()
    }

    private fun generateKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    ALIAS_KEY,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                .setKeySize(KEY_SIZE)
                .setBlockModes(CURRENT_BLOCK_MODE)
                .setEncryptionPaddings(CURRENT_PADDING)
                .build()
            )
        }.generateKey()
    }


    fun encryptStringSillyMode(plainText: String): String {
        val encryptCipher = getEncryptCipher()
        val cipherText = Base64.encodeToString(encryptCipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
        val iv = Base64.encodeToString(
            encryptCipher.parameters.getParameterSpec(GCMParameterSpec::class.java).iv,
            Base64.DEFAULT
        )

        return "$iv$SILLY_SEPARATOR$cipherText"
    }

    fun decryptStringSillyMode(strToDecode: String): String {
        val ivAndCipherTextSplitted = strToDecode.split(SILLY_SEPARATOR)
        val iv = Base64.decode(ivAndCipherTextSplitted[0], Base64.DEFAULT)
        val cipherText = Base64.decode(ivAndCipherTextSplitted[1], Base64.DEFAULT)

        val plainText = getDecryptCipherForIv(iv).doFinal(cipherText)

        return String(plainText, StandardCharsets.UTF_8)
    }



    fun encryptToString(bytes: ByteArray): String {
        val encryptCipher = getEncryptCipher()
        val cipherText = encryptCipher.doFinal(bytes)
        val outputStream = ByteArrayOutputStream()
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(cipherText.size)
            it.write(cipherText)
        }

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    fun decryptFromString(cipherText: String): ByteArray {
        val aa = Base64.decode(cipherText, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(aa)
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val cipherBytesSize = it.read()
            val cipherBytes = ByteArray(cipherBytesSize)
            it.read(cipherBytes)

            getDecryptCipherForIv(iv).doFinal(cipherBytes)
        }
    }


    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray {
        val encryptCipher = getEncryptCipher()
        val cipherText = encryptCipher.doFinal(bytes)
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(cipherText.size)
            it.write(cipherText)
        }
        return cipherText
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val cipherBytesSize = it.read()
            val cipherBytes = ByteArray(cipherBytesSize)
            it.read(cipherBytes)

            getDecryptCipherForIv(iv).doFinal(cipherBytes)
        }
    }
}