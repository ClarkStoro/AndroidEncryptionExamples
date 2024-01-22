package com.clarkstoro.encryptionexample.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import timber.log.Timber
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


class BiometricCryptoManager {


    companion object {

        private const val KEYSTORE = "AndroidKeyStore"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        private const val GCM_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val GCM_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val GCM_TAG_LENGTH = 128

        private const val CBC_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val CBC_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7

        private const val CURRENT_BLOCK_MODE = GCM_BLOCK_MODE
        private const val CURRENT_PADDING = GCM_PADDING

        private const val TRANSFORMATION = "$ALGORITHM/$CURRENT_BLOCK_MODE/$CURRENT_PADDING"

        private const val ALIAS_KEY = "ENCRYPTION_ALIAS_KEY"
        private const val KEY_SIZE = 256

        private const val APPEND_SEPARATOR = "|||"
    }


    private val keystore = KeyStore.getInstance(KEYSTORE).apply {
        load(null)
    }

    /**
     * @return Cipher for Encryption with biometric
     */
    fun getEncryptCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
    }

    /**
     * @return Cipher for Decryption with biometric by obtaining the IV from the text encrypted
     */
    fun getDecryptCipherFromStringAppendMode(encryptedText: String): Cipher {
        val iv = splitEncryptedDataAppendMode(encryptedText).iv
        return getDecryptCipherForIv(iv)
    }

    fun getDecryptCipherFromStringByteArrayMode(encryptedText: String): Cipher {
        val iv = splitEncryptedDataArrayMode(encryptedText).iv
        return getDecryptCipherForIv(iv)
    }

    private fun getDecryptCipherForIv(initializationVector: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            when(CURRENT_BLOCK_MODE) {
                GCM_BLOCK_MODE -> {
                    val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, initializationVector)
                    init(Cipher.DECRYPT_MODE, getKey(), gcmParameterSpec)
                }
                CBC_BLOCK_MODE -> {
                    init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(initializationVector))
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
                .setUserAuthenticationRequired(true)
                .build()
            )
        }.generateKey()
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Append Mode
     */

    fun encryptStringAppendMode(encryptCipher: Cipher, plainText: String): String? {
        return try {
            val cipherText = encryptCipher.doFinal(plainText.toByteArray())
            val cipherTextBase64 = Base64.encodeToString(cipherText, Base64.DEFAULT)
            val ivBase64 = Base64.encodeToString(
                encryptCipher.iv,
                Base64.DEFAULT
            )

            return "$ivBase64$APPEND_SEPARATOR$cipherTextBase64"
        } catch (e: Exception) {
            Timber.e("Error: failed to encrypt string - Append Mode:\n$e")
            null
        }
    }

    fun decryptStringAppendMode(decryptCipher: Cipher, strToDecode: String): String? {
        return try {
            val cipherText = splitEncryptedDataAppendMode(strToDecode).cipherText
            val plainText = decryptCipher.doFinal(cipherText)

            return String(plainText, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string - Append Mode:\n$e")
            null
        }
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Byte Array Mode
     */

    fun encryptToStringByteArrayMode(encryptCipher: Cipher, bytes: ByteArray): String? {
        return try {
            val cipherText = encryptCipher.doFinal(bytes)
            val outputStream = ByteArrayOutputStream()
            outputStream.use {
                it.write(encryptCipher.iv.size)
                it.write(encryptCipher.iv)
                it.write(cipherText.size)
                it.write(cipherText)
            }

            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            Timber.e("Error: failed to encrypt string - Byte Array Mode:\n$e")
            null
        }
    }

    fun decryptFromStringByteArrayMode(decryptCipher: Cipher, stringToDecode: String): ByteArray? {
        return try {
            val cipherTextBytes = splitEncryptedDataArrayMode(stringToDecode).cipherText
            decryptCipher.doFinal(cipherTextBytes)
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string - Byte Array Mode:\n$e")
            null
        }
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Utils
     */

    private fun splitEncryptedDataAppendMode(encryptedText: String): EncryptedData {
        val ivAndCipherTextSplit = encryptedText.split(APPEND_SEPARATOR)
        val iv = Base64.decode(ivAndCipherTextSplit[0], Base64.DEFAULT)
        val cipherText = Base64.decode(ivAndCipherTextSplit[1], Base64.DEFAULT)

        return EncryptedData(iv, cipherText)
    }


    private fun splitEncryptedDataArrayMode(encryptedText: String): EncryptedData {
        val decodedString = Base64.decode(encryptedText, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(decodedString)
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val cipherTextBytesSize = it.read()
            val cipherTextBytes = ByteArray(cipherTextBytesSize)
            it.read(cipherTextBytes)

            EncryptedData(iv, cipherTextBytes)
        }
    }


    data class EncryptedData(
        val iv: ByteArray,
        val cipherText: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as EncryptedData

            if (!iv.contentEquals(other.iv)) return false
            return cipherText.contentEquals(other.cipherText)
        }

        override fun hashCode(): Int {
            var result = iv.contentHashCode()
            result = 31 * result + cipherText.contentHashCode()
            return result
        }
    }
}