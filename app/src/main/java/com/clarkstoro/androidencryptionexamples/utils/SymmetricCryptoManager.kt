package com.clarkstoro.androidencryptionexamples.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


class SymmetricCryptoManager {


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

    private fun getEncryptCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
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
        return getValidKeyOrNull() ?: generateKey()
    }

    private fun getValidKeyOrNull(): SecretKey? {
        return try {
            val existingKey = keystore.getEntry(ALIAS_KEY, null) as? SecretKeyEntry
            existingKey?.secretKey?.also { sk ->
                Cipher.getInstance(TRANSFORMATION).apply {
                    init(Cipher.ENCRYPT_MODE, sk)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isKeyValid(): Boolean {
        return getValidKeyOrNull() != null
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


    fun encryptStringAppendMode(plainText: String): String? {
        return try {
            val encryptCipher = getEncryptCipher()
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

    fun decryptStringAppendMode(strToDecode: String): String? {
        return try {
            val ivAndCipherTextSplit = strToDecode.split(APPEND_SEPARATOR)
            val iv = Base64.decode(ivAndCipherTextSplit[0], Base64.DEFAULT)
            val cipherText = Base64.decode(ivAndCipherTextSplit[1], Base64.DEFAULT)

            val plainText = getDecryptCipherForIv(initializationVector = iv).doFinal(cipherText)

            return String(plainText, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string - Append Mode:\n$e")
            null
        }
    }


    fun encryptToStringByteArrayMode(bytes: ByteArray): String? {
        return try {
            val encryptCipher = getEncryptCipher()
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

    fun decryptFromStringByteArrayMode(stringToDecode: String): ByteArray? {
        return try {
            val decodedString = Base64.decode(stringToDecode, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedString)
            inputStream.use {
                val ivSize = it.read()
                val iv = ByteArray(ivSize)
                it.read(iv)

                val cipherTextBytesSize = it.read()
                val cipherTextBytes = ByteArray(cipherTextBytesSize)
                it.read(cipherTextBytes)

                getDecryptCipherForIv(initializationVector = iv).doFinal(cipherTextBytes)
            }
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string - Byte Array Mode:\n$e")
            null
        }
    }
}